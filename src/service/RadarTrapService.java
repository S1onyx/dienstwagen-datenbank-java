package service;

import model.Car;
import model.Driver;
import model.Trip;
import utils.DateParserUtils;
import utils.EntityFinderUtils;

import java.time.LocalDateTime;
import java.util.List;

public class RadarTrapService {

    private final List<Driver> drivers;
    private final List<Car> cars;
    private final List<Trip> trips;

    public RadarTrapService(List<Driver> drivers, List<Car> cars, List<Trip> trips) {
        this.drivers = drivers;
        this.cars = cars;
        this.trips = trips;
    }

    // Find the driver of a car at a specific timestamp
    public String findDriverAtTime(String input) {
        StringBuilder result = new StringBuilder();

        if (!input.contains(";"))
            throw new IllegalArgumentException("Ungültiges Format. Erwartet: S-XX-1234;2024-02-14T13:57:43");

        String[] parts = input.split(";", 2);
        String licensePlate = parts[0].trim();
        LocalDateTime timestamp = DateParserUtils.parseDateTime(parts[1].trim());

        Car car = EntityFinderUtils.findCarByLicensePlate(cars, licensePlate);

        // Find trips matching the car and timestamp
        List<Trip> matchingTrips = trips.stream()
                .filter(t -> t.carId().equals(car.id()) && t.includesTime(timestamp))
                .toList();

        if (matchingTrips.isEmpty()) {
            return "Kein Fahrer gefunden.\n";
        }

        // Print all matches
        for (Trip trip : matchingTrips) {
            Driver driver = EntityFinderUtils.findDriverById(drivers, trip.driverId());
            // result.append("\nGeblitzt während folgender Fahrt:\n");
            // result.append("Fahrzeug: ").append(car).append("\n");
            // result.append("Fahrt: ").append(trip).append("\n");
            // result.append("Fahrer: ").append(driver).append("\n");
            result.append("Fahrer: ").append(driver.getFullName()).append("\n");
        }

        return result.toString();
    }
}