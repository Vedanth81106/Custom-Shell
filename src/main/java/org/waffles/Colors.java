package org.waffles;

public enum Colors {
    // Enum Constants
    RESET("\033[0m"),

    // Standard
    BLACK("\033[0;30m"),
    RED("\033[0;31m"),
    GREEN("\033[0;32m"),
    YELLOW("\033[0;33m"),
    BLUE("\033[0;34m"),
    PURPLE("\033[0;35m"),
    CYAN("\033[0;36m"),
    WHITE("\033[0;37m"),

    // Bold
    RED_BOLD("\033[1;31m"),
    GREEN_BOLD("\033[1;32m"),
    CYAN_BOLD("\033[1;36m");

    private final String code;

    Colors(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}