package exceptions;

/**
 * Wird geworfen, wenn die Eingabe ungültig ist, z. B. falsches Format oder unzulässiger Wert.
 * <p>
 * Beispiel: Fehlerhaftes Datumsformat oder falsche Argumente.
 */
public class InvalidInputException extends RuntimeException {

    /**
     * Erstellt eine neue Ausnahme mit einer benutzerdefinierten Nachricht.
     *
     * @param message Fehlermeldung zur Beschreibung der fehlerhaften Eingabe
     */
    public InvalidInputException(String message) {
        super(message);
    }
}