package utils;

public class ArgParserUtils {
    public static String extractValue(String arg) {
        return arg.split("=", 2)[1].trim();
    }
}