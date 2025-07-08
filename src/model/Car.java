package model;

/**
 * Repräsentiert ein Fahrzeug mit eindeutiger ID, Hersteller, Modell und Kennzeichen.
 *
 * Diese Klasse verwendet das Record-Konstrukt für eine unveränderliche Datenstruktur.
 *
 * @param id            Eindeutige Fahrzeug-ID
 * @param manufacturer  Hersteller des Fahrzeugs
 * @param model         Modellbezeichnung
 * @param licensePlate  Kennzeichen
 */
public record Car(
        String id,
        String manufacturer,
        String model,
        String licensePlate
) {

    /**
     * Gibt eine formatierte Zeichenkette mit Fahrzeugdaten zurück.
     *
     * @return Formatierter String z. B. "C001: BMW 320i (S-XX-1234)"
     */
    @Override
    public String toString() {
        return "%s: %s %s (%s)".formatted(id, manufacturer, model, licensePlate);
    }
}