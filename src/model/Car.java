package model;

public record Car(String id, String manufacturer, String model, String licensePlate) {

    // Returns formatted display name of the car
    public String getDisplayName() {
        return "%s %s (%s)".formatted(manufacturer, model, licensePlate);
    }

    @Override
    public String toString() {
        return "%s: %s %s (%s)".formatted(id, manufacturer, model, licensePlate);
    }
}