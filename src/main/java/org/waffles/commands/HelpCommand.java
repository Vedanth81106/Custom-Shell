package org.waffles.commands;

import java.io.IOException;

public class HelpCommand implements Command{
    @Override
    public void execute(String... args) throws IOException {
        System.out.println("\n=== CUSTOM SHELL HELP ===");

        // Simple Commands
        System.out.printf("%-15s %s\n", "google <q>", "Search Google.");
        System.out.printf("%-15s %s\n", "yt <q>", "Search YouTube.");
        System.out.printf("%-15s %s\n", "spotify <q>", "Open/Search Spotify.");
        System.out.printf("%-15s %s\n", "steam", "Launch Steam.");
        System.out.printf("%-15s %s\n", "discord", "Launch Discord.");

        // Complex Commands
        System.out.printf("%-15s %s\n", "math", "Calculate nums (e.g., math 5 + 5).");
        System.out.printf("%-15s %s\n", "ai", "Chat with AI (Try 'ai --help' for more).");

        // System
        System.out.printf("%-15s %s\n", "exit", "Close the shell.");
        System.out.println("=========================\n");
    }
}
