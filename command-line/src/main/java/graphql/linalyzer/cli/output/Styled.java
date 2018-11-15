package graphql.linalyzer.cli.output;

public class Styled {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";

    private static final String UNDERLINED = "\u001B[4m";
    private static final String BOLD = "\u001B[1m";

    public static String red(String text) {
        return RED + text + ANSI_RESET;
    }

    public static String white(String text) {
        return WHITE + text + ANSI_RESET;
    }

    public static String blue(String text) {
        return BLUE + text + ANSI_RESET;
    }

    public static String yellow(String text) {
        return YELLOW + text + ANSI_RESET;
    }

    public static String underlined(String text) {
        return UNDERLINED + text + ANSI_RESET;
    }

    public static String bold(String text) {
        return BOLD + text + ANSI_RESET;
    }

}
