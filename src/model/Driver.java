package model;

public record Driver(String id, String firstName, String lastName, LicenseClass licenseClass) {
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "%s %s (%s, Klasse %s)".formatted(firstName, lastName, id, licenseClass);
    }
}