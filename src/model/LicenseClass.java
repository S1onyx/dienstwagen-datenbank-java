package model;

/**
 * Aufzählung aller unterstützten Führerscheinklassen.
 */
public enum LicenseClass {
    A, B, C, D, BE, CE, DE, AM, A1, A2;

    /**
     * Wandelt eine Zeichenkette in eine gültige Lizenzklasse um.
     *
     * @param value Eingabestring (z. B. "b", "BE")
     * @return Passende Lizenzklasse
     * @throws IllegalArgumentException falls die Klasse unbekannt ist
     */
    public static LicenseClass fromString(String value) {
        try {
            return LicenseClass.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unbekannte Führerscheinklasse: " + value);
        }
    }
}