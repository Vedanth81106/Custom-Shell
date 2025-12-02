package org.waffles.commands;

import com.google.gson.Gson;
import org.waffles.Colors; // Import your Colors enum
import org.waffles.models.AiModels;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Properties;

public class AiCommand implements Command {

    private final Gson gson = new Gson();
    private final HttpClient client = HttpClient.newHttpClient();
    private String currentModel = "auto";

    @Override
    public void execute(String... args) throws IOException {

        if (args.length < 2) {
            System.out.println(Colors.RED + "Incorrect command. Did you mean:\n ai <prompt>" +
                    "\nai --default\nai --gemini\nai --ollama" + Colors.RESET);
            return;
        }

        String firstArg = args[1];
        firstArg = firstArg.trim().toLowerCase();

        switch (firstArg) {

            case "--debug":
                listAvailableModels();
                return;

            case "--help":
                System.out.println(Colors.CYAN_BOLD + "\n--- AI COMMAND USAGE ---" + Colors.RESET);
                System.out.println(Colors.GREEN + "1. Chat (Auto-Failover):" + Colors.RESET);
                System.out.println("   ai <your prompt>");
                System.out.println(Colors.GREEN + "\n2. Force a specific Model (One-time):" + Colors.RESET);
                System.out.println("   ai --gemini <prompt>");
                System.out.println("   ai --ollama <prompt>");
                System.out.println(Colors.GREEN + "\n3. Change Default Model:" + Colors.RESET);
                System.out.println("   ai --default gemini");
                System.out.println("   ai --default ollama");
                System.out.println("   ai --default auto");
                return;

            case "--gemini":
                if (args.length < 3) return;
                String geminiPrompt = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                standardChat("gemini", geminiPrompt);
                return;

            case "--ollama":
                if (args.length < 3) return;
                String ollamaPrompt = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                standardChat("ollama", ollamaPrompt);
                return;

            case "--default":
                if (args.length < 3) {
                    System.out.println(Colors.YELLOW + "Usage: ai --default [gemini/ollama/auto]" + Colors.RESET);
                    return;
                }

                if (args[2].isEmpty()) {
                    System.out.println(Colors.RED + "Incorrect command." + Colors.RESET);
                }

                String model = args[2].trim().toLowerCase();
                if (!model.equals("gemini") && !model.equals("ollama") && !model.equals("auto")) {
                    System.out.println(Colors.RED + "Invalid model. Choose: gemini, ollama, auto" + Colors.RESET);
                    return;
                }

                currentModel = model;
                System.out.println(Colors.GREEN + "Default AI set to: " + model + Colors.RESET);
                return;

            default:
                // If it starts with -- but not valid command
                if (firstArg.startsWith("--")) {
                    System.out.println(Colors.YELLOW + "Unknown flag: " + firstArg + Colors.RESET);
                    System.out.println("Try 'ai --help' to see valid options.");
                    return;
                }
                break;
        }

        String prompt = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        standardChat(currentModel, prompt);
    }

    private void standardChat(String model, String prompt) {
        model = model.trim().toLowerCase();

        if ("gemini".equals(model)) {
            try {
                chatWithGemini(prompt);
            } catch (Exception e) {
                System.out.println(Colors.RED + "Chat with Gemini failed. " + e.getMessage() + Colors.RESET);
            }
        } else if ("ollama".equals(model)) {
            try {
                chatWithOllama(prompt);
            } catch (Exception e) {
                System.out.println(Colors.RED + "Chat with Ollama failed. " + e.getMessage() + Colors.RESET);
            }
        } else if ("auto".equals(model)) {
            try {
                chatWithOllama(prompt);
            } catch (Exception e) {
                System.out.println(Colors.YELLOW + "Local AI failed (" + e.getMessage() + "). Switching to Cloud (Gemini)..." + Colors.RESET);
                try {
                    chatWithGemini(prompt);
                } catch (Exception cloudError) {
                    System.out.println(Colors.RED + "Error: Both AIs failed. " + cloudError.getMessage() + Colors.RESET);
                }
            }
        }
    }

    private void chatWithOllama(String prompt) throws IOException, InterruptedException {

        AiModels.OllamaRequest ollamaRequest = new AiModels.OllamaRequest("tinyllama", prompt);
        String jsonBody = gson.toJson(ollamaRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:11434/api/generate"))
                .header("CONTENT-TYPE", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Ollama error: " + response.statusCode());
        }

        AiModels.OllamaResponse responseObj = gson.fromJson(response.body(), AiModels.OllamaResponse.class);

        if (responseObj == null || responseObj.response == null || responseObj.response.isEmpty()) {
            throw new IOException("Ollama returned empty response.");
        }

        System.out.println(Colors.PURPLE + "[Ollama]: " + Colors.RESET + responseObj.response);
    }

    private void chatWithGemini(String prompt) throws IOException, InterruptedException {
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
        System.out.println(Colors.PURPLE + "[Gemini]: " + Colors.RESET + responseObj.getAnswer());
    }

    private void listAvailableModels() throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            throw new IOException("Could not find config.properties file!");
        }

        String key = props.getProperty("gemini_key");
        String url = "https://generativelanguage.googleapis.com/v1beta/models?key=" + key;

        System.out.println(Colors.CYAN + "Querying Google for available models..." + Colors.RESET);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(Colors.GREEN + "--- GOOGLE RESPONSE ---" + Colors.RESET);
            System.out.println(response.body());
            System.out.println(Colors.GREEN + "-----------------------" + Colors.RESET);

        } catch (Exception e) {
            System.out.println(Colors.RED + "Debug failed: " + e.getMessage() + Colors.RESET);
        }
    }
}