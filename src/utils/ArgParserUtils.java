package utils;

/**
 * Dienstklasse zum Parsen von Kommandozeilenargumenten im Format <code>--key=value</code>.
 */
public class ArgParserUtils {

    /**
     * Extrahiert den Wert aus einem Argument im Format <code>--key=value</code>.
     *
     * @param arg Das vollständige Argument
     * @return Der extrahierte Wert, bereinigt von Anführungszeichen
     * @throws ArrayIndexOutOfBoundsException bei ungültigem Format
     */
    public static String extractValue(String arg) {
        return arg.split("=", 2)[1].trim().replace("\"", "");
    }
}