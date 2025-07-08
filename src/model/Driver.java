package model;

/**
 * Repräsentiert einen Fahrer mit Vor- und Nachnamen, ID und Führerscheinklasse.
 *
 * Diese Klasse verwendet das Record-Konstrukt für eine unveränderliche Datenstruktur.
 *
 * @param id           Eindeutige Fahrer-ID
 * @param firstName    Vorname des Fahrers
 * @param lastName     Nachname des Fahrers
 * @param licenseClass Führerscheinklasse
 */
public record Driver(
        String id,
        String firstName,
        String lastName,
        LicenseClass licenseClass
) {

    /**
     * Gibt den vollständigen Namen des Fahrers zurück.
     *
     * @return Vorname + Nachname als ein String
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Gibt eine formatierte Zeichenkette mit Fahrerinformationen zurück.
     *
     * @return Formatierter String mit Name, ID und Führerscheinklasse
     */
    @Override
    public String toString() {
        return "%s %s (%s, Klasse %s)".formatted(firstName, lastName, id, licenseClass);
    }
}