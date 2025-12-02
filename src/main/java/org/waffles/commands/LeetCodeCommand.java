package org.waffles.commands;

import com.google.gson.Gson;
import org.waffles.Colors;
import org.waffles.models.LeetcodeModels; // Import models
import org.waffles.shell.ShellUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class LeetCodeCommand implements Command {

    private final Scanner scanner = new Scanner(System.in);
    private final Gson gson = new Gson();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private static final String QUERY =
            "query randomQuestion { randomQuestion(categorySlug: \"\", filters: {})" +
                    " { title titleSlug difficulty } }";

    @Override
    public void execute(String... args) throws IOException {
        System.out.println(Colors.CYAN + "Entering LeetCode Mode..." + Colors.RESET);

        while (true) {
            System.out.println("Fetching random question...");

            // 1. Fetch the data first
            LeetcodeModels.Question question = fetchQuestion();

            if (question == null) {
                System.out.println("Failed to fetch question. Retrying...");
                try { Thread.sleep(2000); } catch (InterruptedException e) {} // Wait 2 seconds
                continue;
            }

            // 2. Display the info
            String color = Colors.GREEN.toString();
            if ("Medium".equalsIgnoreCase(question.difficulty)) color = Colors.YELLOW.toString();
            if ("Hard".equalsIgnoreCase(question.difficulty)) color = Colors.RED.toString();

            System.out.println("\nChallenge: " + Colors.CYAN_BOLD + question.title + Colors.RESET);
            System.out.println("Difficulty: " + color + question.difficulty + Colors.RESET);

            // 3. Ask for permission
            System.out.print("\nOpen (y) / Reroll (n) / Exit (e)? ");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("y")) {
                System.out.println("Opening browser...");
                String url = "https://leetcode.com/problems/" + question.titleSlug;
                ShellUtils.cmdStarter("cmd.exe", "/c", "start", url);
                break; // Exit after opening
            } else if (choice.equals("n")) {
                System.out.println("Rerolling...");
                continue;
            } else if (choice.equals("e")) {
                break;
            }
        }
    }

    private LeetcodeModels.Question fetchQuestion() {
        try {
            LeetcodeModels.GraphRequest graphRequest = new LeetcodeModels.GraphRequest(QUERY);
            String jsonBody = gson.toJson(graphRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://leetcode.com/graphql"))
                    .header("Content-Type", "application/json")
                    .header("User-Agent", "Mozilla/5.0")
                    .header("Referer", "https://leetcode.com")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println(Colors.RED + "Leetcode Error Code: " + response.statusCode() + Colors.RESET);
                return null;
            }

            LeetcodeModels.GraphResponse graphResponse = gson.fromJson(response.body(), LeetcodeModels.GraphResponse.class);

            if (graphResponse == null || graphResponse.data == null || graphResponse.data.randomQuestion == null) {
                return null;
            }

            return graphResponse.data.randomQuestion;

        } catch (Exception e) {
            System.out.println(Colors.RED + "Network Error: " + e.getMessage() + Colors.RESET);
            return null;
        }
    }
}