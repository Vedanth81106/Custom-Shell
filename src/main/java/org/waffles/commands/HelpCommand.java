package org.waffles.commands;

import java.io.IOException;
import org.waffles.Colors;

public class HelpCommand implements Command{
    @Override
    public void execute(String... args) {
        System.out.println(Colors.CYAN_BOLD + "\n=== CUSTOM SHELL HELP ===" + Colors.RESET);
        System.out.println("Here are the available commands:\n");

        // Section 1: Automation & AI
        System.out.println(Colors.YELLOW + "--- Automation & AI ---" + Colors.RESET);
        System.out.printf("%-25s %s\n", Colors.PURPLE + "do <request>" + Colors.RESET, "Execute commands via natural language (Actions).");
        System.out.printf("%-25s %s\n", Colors.PURPLE + "ai <prompt>" + Colors.RESET, "Chat with the AI (Conversation).");

        // Section 2: Developer Tools
        System.out.println(Colors.YELLOW + "\n--- Developer Tools ---" + Colors.RESET);
        System.out.printf("%-25s %s\n", Colors.CYAN + "leetcode" + Colors.RESET, "Get a random coding challenge.");
        System.out.printf("%-25s %s\n", Colors.CYAN + "math <expr>" + Colors.RESET, "Calculate numbers (e.g. 5 * 5).");

        // Section 3: Media & Web
        System.out.println(Colors.YELLOW + "\n--- Media & Web ---" + Colors.RESET);
        System.out.printf("%-25s %s\n", Colors.RED + "yt <query>" + Colors.RESET, "Search YouTube.");
        System.out.printf("%-25s %s\n", Colors.BLUE + "google <query>" + Colors.RESET, "Search Google.");
        System.out.printf("%-25s %s\n", Colors.GREEN + "spotify <query>" + Colors.RESET, "Open/Search Spotify.");
        System.out.printf("%-25s %s\n", Colors.BLUE + "steam" + Colors.RESET, "Launch Steam.");
        System.out.printf("%-25s %s\n", Colors.PURPLE + "discord" + Colors.RESET, "Launch Discord.");

        // Section 4: System
        System.out.println(Colors.YELLOW + "\n--- System ---" + Colors.RESET);
        System.out.printf("%-25s %s\n", Colors.WHITE + "exit" + Colors.RESET, "Close the shell.");

        System.out.println(Colors.CYAN_BOLD + "=========================\n" + Colors.RESET);
    }
}
