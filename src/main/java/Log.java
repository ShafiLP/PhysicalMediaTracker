public class Log {
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String RESET  = "\u001B[0m";

    /**
     * Prints text in console.
     * @param s Text to print in console.
     */
    static void print(String s){
        System.out.println(s);
    }

    /**
     * Prints info text in console, formatted as "[INFO] ...".
     * @param s Info text to print in console.
     */
    static void info(String s){
        System.out.println("[INFO] " + s);
    }

    /**
     * Prints warning text in console, formatted as "[WARN] ..." in yellow colour.
     * @param s Warning text to print in console.
     */
    static void warn(String s){
        System.out.println(YELLOW + "[WARNING] " + s + RESET);
    }

    /**
     * Prints error text in console, formatted as "[ERROR] ..." in red colour.
     * @param s Error text to print in console.
     */
    static void error(String s){
        System.err.println(RED + "[ERROR] " + s + RESET);
    }
}
