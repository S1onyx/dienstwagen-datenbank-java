package model;

public enum LicenseClass {
    A, B, C, D, BE, CE, DE, AM, A1, A2;

    // Converts string to enum, case-insensitive
    public static LicenseClass fromString(String value) {
        try {
            return LicenseClass.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unbekannte Führerscheinklasse: " + value);
        }
    }
}