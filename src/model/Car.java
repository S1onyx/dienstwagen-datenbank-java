package model;

public class Car {
    private final String id;
    private final String manufacturer;
    private final String model;
    private final String licensePlate;

    public Car(String id, String manufacturer, String model, String licensePlate) {
        this.id = id;
        this.manufacturer = manufacturer.trim();
        this.model = model.trim();
        this.licensePlate = licensePlate.trim();
    }

    public String getId() {
        return id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    @Override
    public String toString() {
        return manufacturer + " " + model + " (" + licensePlate + ")";
    }
}