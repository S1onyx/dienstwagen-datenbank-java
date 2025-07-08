package exceptions;

/**
 * Wird geworfen, wenn eine Entität mit derselben ID bereits vorhanden ist.
 * <p>
 * Beispiel: Zwei Fahrer mit identischer Fahrer-ID beim Import.
 */
public class DuplicateEntityException extends RuntimeException {

    /**
     * Erstellt eine neue Ausnahme mit einer benutzerdefinierten Nachricht.
     *
     * @param message Fehlermeldung zur Beschreibung des Problems
     */
    public DuplicateEntityException(String message) {
        super(message);
    }
}