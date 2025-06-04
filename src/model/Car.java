package model;

public record Car(
        String id,
        String manufacturer,
        String model,
        String licensePlate
) {

    // Returns formatted car description
    @Override
    public String toString() {
        return "%s: %s %s (%s)".formatted(id, manufacturer, model, licensePlate);
    }
}