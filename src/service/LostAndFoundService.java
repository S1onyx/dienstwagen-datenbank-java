package service;

import model.Car;
import model.Driver;
import model.Trip;

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

    public void findOtherDrivers(String input) {
        if (!input.contains(";")) {
            System.out.println("Ungültiges Format. Erwartet: F003;2024-08-13");
            return;
        }

        String[] parts = input.split(";", 2);
        String driverId = parts[0].trim();
        String dateString = parts[1].trim();

        LocalDate date;
        try {
            date = LocalDate.parse(dateString);
        } catch (Exception e) {
            System.out.println("Fehler: Datum muss im Format yyyy-MM-dd angegeben sein.");
            return;
        }

        Optional<Driver> originalDriverOpt = drivers.stream()
                .filter(d -> d.getId().equals(driverId))
                .findFirst();

        if (originalDriverOpt.isEmpty()) {
            System.out.println("Fahrer mit ID \"" + driverId + "\" nicht gefunden.");
            return;
        }

        Driver originalDriver = originalDriverOpt.get();

        // Fahrzeuge, die der Fahrer an diesem Tag gefahren hat
        Set<String> carIds = trips.stream()
                .filter(t -> t.getDriverId().equals(driverId) && t.getStartTime().toLocalDate().equals(date))
                .map(Trip::getCarId)
                .collect(Collectors.toSet());

        if (carIds.isEmpty()) {
            System.out.println("Keine Fahrten für Fahrer " + driverId + " am " + date + " gefunden.");
            return;
        }

        Set<String> otherDriverEntries = new HashSet<>();

        for (String carId : carIds) {
            List<Trip> tripsWithSameCar = trips.stream()
                    .filter(t -> t.getCarId().equals(carId) &&
                            t.getStartTime().toLocalDate().equals(date) &&
                            !t.getDriverId().equals(driverId))
                    .toList();

            for (Trip trip : tripsWithSameCar) {
                Driver otherDriver = drivers.stream()
                        .filter(d -> d.getId().equals(trip.getDriverId()))
                        .findFirst()
                        .orElse(null);

                if (otherDriver != null) {
                    Car car = cars.stream()
                            .filter(c -> c.getId().equals(carId))
                            .findFirst()
                            .orElse(null);

                    if (car != null) {
                        String entry = otherDriver.getFirstName() + " " + otherDriver.getLastName()
                                + " (ID: " + otherDriver.getId() + ", " + car.getLicensePlate() + ")";
                        otherDriverEntries.add(entry);
                    }
                }
            }
        }

        System.out.println("\nFahrer hat etwas im Fahrzeug liegen lassen!");
        System.out.println("Tag: " + date);
        System.out.println("Ursprünglicher Fahrer: " + originalDriver.getFirstName() + " " + originalDriver.getLastName()
                + " (ID: " + driverId + ")");
        System.out.println("Fahrzeuge an diesem Tag: " + carIds.stream()
                .map(id -> cars.stream().filter(c -> c.getId().equals(id)).findFirst().map(Car::getLicensePlate).orElse("Unbekannt"))
                .collect(Collectors.joining(", ")));

        if (otherDriverEntries.isEmpty()) {
            System.out.println("Keine anderen Fahrer gefunden.");
        } else {
            System.out.println("Andere Fahrer dieser Fahrzeuge:");
            otherDriverEntries.stream()
                    .sorted()
                    .forEach(entry -> System.out.println("• " + entry));
        }
    }
}