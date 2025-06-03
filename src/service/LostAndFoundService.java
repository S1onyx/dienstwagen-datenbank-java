package service;

import exceptions.InvalidInputException;
import model.Car;
import model.Driver;
import model.Trip;
import utils.DateParser;
import utils.EntityFinder;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class LostAndFoundService {

    private final List<Driver> drivers;
    private final List<Car> cars;
    private final List<Trip> trips;

    public LostAndFoundService(List<Driver> drivers, List<Car> cars, List<Trip> trips) {
        this.drivers = drivers;
        this.cars = cars;
        this.trips = trips;
    }

    // Find other drivers who used the same vehicle on the same day
    public void findOtherDrivers(String input) {
        if (!input.contains(";"))
            throw new InvalidInputException("Ungültiges Format. Erwartet: F003;2024-08-13");

        String[] parts = input.split(";", 2);
        String driverId = parts[0].trim();
        LocalDate date = DateParser.parseDate(parts[1].trim());

        Driver originalDriver = EntityFinder.findDriverById(drivers, driverId);

        // Collect all cars used by the driver on that date
        Set<String> carIds = trips.stream()
                .filter(t -> t.driverId().equals(driverId) && t.isOnDate(date))
                .map(Trip::carId)
                .collect(Collectors.toSet());

        if (carIds.isEmpty()) {
            System.out.printf("Keine Fahrten für Fahrer %s am %s gefunden.%n", driverId, date);
            return;
        }

        Set<String> otherDriverEntries = new TreeSet<>();

        // Check other drivers using the same cars on the same day
        for (String carId : carIds) {
            List<Trip> sameDayTrips = trips.stream()
                    .filter(t -> t.carId().equals(carId) && t.isOnDate(date) && !t.driverId().equals(driverId))
                    .toList();

            for (Trip trip : sameDayTrips) {
                Driver otherDriver = EntityFinder.findDriverById(drivers, trip.driverId());
                Car car = EntityFinder.findCarById(cars, carId);

                otherDriverEntries.add("%s (ID: %s, %s)"
                        .formatted(otherDriver.getFullName(), otherDriver.id(), car.licensePlate()));
            }
        }

        // Output result
        System.out.println("\nFahrer hat etwas im Fahrzeug liegen lassen!");
        System.out.println("Tag: " + date);
        System.out.printf("Ursprünglicher Fahrer: %s (ID: %s)%n", originalDriver.getFullName(), driverId);
        System.out.print("Fahrzeuge an diesem Tag: ");
        System.out.println(carIds.stream()
                .map(id -> cars.stream().filter(c -> c.id().equals(id)).findFirst().map(Car::licensePlate).orElse("Unbekannt"))
                .collect(Collectors.joining(", ")));

        if (otherDriverEntries.isEmpty()) {
            System.out.println("Keine anderen Fahrer gefunden.");
        } else {
            System.out.println("Andere Fahrer dieser Fahrzeuge:");
            otherDriverEntries.forEach(entry -> System.out.println("• " + entry));
        }
    }
}