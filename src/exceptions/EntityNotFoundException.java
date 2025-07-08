package exceptions;

/**
 * Wird geworfen, wenn eine angeforderte Entität (z. B. Fahrer oder Fahrzeug) nicht gefunden werden konnte.
 * <p>
 * Beispiel: Zugriff auf nicht existierende Fahrer-ID.
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * Erstellt eine neue Ausnahme mit einer benutzerdefinierten Nachricht.
     *
     * @param message Fehlermeldung zur Beschreibung des Problems
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}