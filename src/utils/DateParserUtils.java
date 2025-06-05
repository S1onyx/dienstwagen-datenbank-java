package utils;

import exceptions.InvalidInputException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class DateParserUtils {

    // Parses ISO LocalDateTime or throws InvalidInputException
    public static LocalDateTime parseDateTime(String input) {
        try {
            return LocalDateTime.parse(input);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Datum muss im Format yyyy-MM-ddTHH:mm:ss angegeben sein." + input);
        }
    }

    // Parses ISO LocalDate or throws InvalidInputException
    public static LocalDate parseDate(String input) {
        try {
            return LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Datum muss im Format yyyy-MM-dd angegeben sein.");
        }
    }
}