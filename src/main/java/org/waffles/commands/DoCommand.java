package org.waffles.commands;

import com.google.gson.Gson;
import org.waffles.Colors;
import org.waffles.models.AiModels;
import org.waffles.shell.Shell;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

import static org.waffles.shell.Shell.cmds;

public class DoCommand implements Command{

    private final Gson gson = new Gson();
    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public void execute(String... args) throws IOException, InterruptedException {

        if(args.length < 2){
            System.out.println(Colors.RED + "do Command usage: do <prompt>" + Colors.RESET);
            return;
        }

        String userRequest = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        String instruction = "You are a CLI translator for a custom Windows shell.\n" +
                "Your job is to translate user requests into executable commands.\n" +
                "Available Internal Commands:\n" +
                "- yt <query> (Search YouTube)\n" +
                "- google <query> (Search Google)\n" +
                "- leetcode (Get random question)\n" +
                "- spotify <query> (Spotify)\n" +
                "Rules:\n" +
                "1. If the request matches an Internal Command, return THAT command (e.g. 'yt cat videos').\n" +
                "2. If it is a system task (like 'ping google' or 'list files'), return the Windows CMD command (e.g. 'ping google.com' or 'dir').\n" +
                "3. FOR WINDOWS APPS: If the user asks to open an app like WhatsApp, Calculator, or Settings, use the 'start protocol:' syntax (e.g. 'start whatsapp:', 'start calculator:', 'start ms-settings:').\n" +
                "4. If the user asks to RUN a server (like Postgres), return the docker command (e.g. 'docker run -p 5432:5432 postgres').\n" +
                "5. IMPORTANT: Return ONLY the command text. No markdown, no explanations, no code blocks ans no comments" +
                "\n The user request is: " + userRequest;


        String rawResponse = fetchGeminiResponse(instruction);

        String commandToRun = rawResponse
                .replace("```bash", "")
                .replace("```", "")
                .replaceAll("\u001B\\[[;\\d]*m", "")
                .trim();

        String[] parts = commandToRun.split("\\s+");
        String cmdName = parts[0];

        if(cmds.containsKey(cmdName)){
            cmds.get(cmdName).execute(parts);
        }else{
            try {   
                ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", commandToRun);
                pb.inheritIO();
                pb.start().waitFor();
            } catch (Exception e) {
                System.out.println(Colors.RED + "Execution failed: " + e.getMessage() + Colors.RESET);
            }        }
    }

    private String fetchGeminiResponse(String prompt) throws IOException, InterruptedException {

        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            throw new IOException("Could not find config.properties file!");
        }

        String key = props.getProperty("gemini_key");
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-lite-latest:generateContent?key=" + key;

        AiModels.GeminiRequest geminiRequest = new AiModels.GeminiRequest(prompt);
        String jsonBody = gson.toJson(geminiRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("CONTENT-TYPE", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.out.println(Colors.RED + "Gemini API Error: " + response.body() + Colors.RESET);
            throw new IOException("Gemini error code: " + response.statusCode());
        }

        AiModels.GeminiResponse responseObj = gson.fromJson(response.body(), AiModels.GeminiResponse.class);

        if (responseObj == null || responseObj.candidates == null || responseObj.candidates.isEmpty()) {
            throw new IOException("Gemini returned empty response.");
        }

        return responseObj.getAnswer();
    }
}
