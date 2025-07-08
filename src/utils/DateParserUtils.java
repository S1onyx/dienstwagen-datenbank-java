package utils;

import exceptions.InvalidInputException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Hilfsklasse zur sicheren Umwandlung von Datum-/Zeit-Strings in Java-Zeitobjekte.
 */
public class DateParserUtils {

    /**
     * Parst eine Zeichenkette im Format <code>yyyy-MM-ddTHH:mm:ss</code> in ein {@link LocalDateTime}-Objekt.
     *
     * @param input Datum und Zeit im ISO-Format (z. B. 2024-08-13T14:00:00)
     * @return entsprechendes {@link LocalDateTime}-Objekt
     * @throws InvalidInputException bei ungültigem Format
     */
    public static LocalDateTime parseDateTime(String input) {
        try {
            return LocalDateTime.parse(input);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Datum muss im Format yyyy-MM-ddTHH:mm:ss angegeben sein." + input);
        }
    }

    /**
     * Parst eine Zeichenkette im Format <code>yyyy-MM-dd</code> in ein {@link LocalDate}-Objekt.
     *
     * @param input Datum im ISO-Format (z. B. 2024-08-13)
     * @return entsprechendes {@link LocalDate}-Objekt
     * @throws InvalidInputException bei ungültigem Format
     */
    public static LocalDate parseDate(String input) {
        try {
            return LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Datum muss im Format yyyy-MM-dd angegeben sein.");
        }
    }
}