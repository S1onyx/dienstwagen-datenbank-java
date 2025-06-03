package utils;

public class ArgParserUtils {

    // Extracts value from argument of form --key=value
    public static String extractValue(String arg) {
        return arg.split("=", 2)[1].trim();
    }
}