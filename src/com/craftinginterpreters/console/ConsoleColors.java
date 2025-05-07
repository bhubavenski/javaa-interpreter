package com.craftinginterpreters.console;

public final class ConsoleColors {
    // Reset
    public static final String RESET = "\u001B[0m";

    // Regular Colors
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Bold
    public static final String BOLD = "\u001B[1m";

    // Underline
    public static final String UNDERLINE = "\u001B[4m";

    // Backgrounds
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_PURPLE = "\u001B[45m";
    public static final String BG_CYAN = "\u001B[46m";
    public static final String BG_WHITE = "\u001B[47m";

    // Color methods
    public static String red(Object input) {
        return RED + input + RESET;
    }

    public static String green(Object input) {
        return GREEN + input + RESET;
    }

    public static String yellow(Object input) {
        return YELLOW + input + RESET;
    }

    public static String blue(Object input) {
        return BLUE + input + RESET;
    }

    public static String purple(Object input) {
        return PURPLE + input + RESET;
    }

    public static String cyan(Object input) {
        return CYAN + input + RESET;
    }

    public static String white(Object input) {
        return WHITE + input + RESET;
    }

    public static String bold(Object input) {
        return BOLD + input + RESET;
    }

    public static String underline(Object input) {
        return UNDERLINE + input + RESET;
    }

    public static String redBg(Object input) {
        return BG_RED + input + RESET;
    }

    public static String greenBg(Object input) {
        return BG_GREEN + input + RESET;
    }

    public static String yellowBg(Object input) {
        return BG_YELLOW + input + RESET;
    }

    public static String blueBg(Object input) {
        return BG_BLUE + input + RESET;
    }

    public static String cyanBg(Object input) {
        return BG_CYAN + input + RESET;
    }

    public static String purpleBg(Object input) {
        return BG_PURPLE + input + RESET;
    }

    public static String whiteBg(Object input) {
        return BG_WHITE + input + RESET;
    }

    private ConsoleColors() {
        // Приватен конструктор, за да не може да се инстанцира
    }
}
