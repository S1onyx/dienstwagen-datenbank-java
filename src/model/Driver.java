package model;

public class Driver {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String licenseClass;

    public Driver(String id, String firstName, String lastName, String licenseClass) {
        this.id = id;
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.licenseClass = licenseClass.trim();
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLicenseClass() {
        return licenseClass;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + id + ", Klasse " + licenseClass + ")";
    }
}