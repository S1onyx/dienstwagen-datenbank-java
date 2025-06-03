package utils;

import exceptions.EntityNotFoundException;
import model.Car;
import model.Driver;

import java.util.List;

public class EntityFinder {

    // Find driver by ID or throw exception
    public static Driver findDriverById(List<Driver> drivers, String id) {
        return drivers.stream()
                .filter(d -> d.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Fahrer mit ID \"" + id + "\" nicht gefunden."));
    }

    // Find car by ID or throw exception
    public static Car findCarById(List<Car> cars, String id) {
        return cars.stream()
                .filter(c -> c.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Fahrzeug mit ID \"" + id + "\" nicht gefunden."));
    }

    // Find car by license plate or throw exception
    public static Car findCarByLicensePlate(List<Car> cars, String licensePlate) {
        return cars.stream()
                .filter(c -> c.licensePlate().equalsIgnoreCase(licensePlate))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Kein Fahrzeug mit dem Kennzeichen \"" + licensePlate + "\" gefunden."));
    }
}