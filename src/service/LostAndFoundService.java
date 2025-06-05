package service;

import exceptions.InvalidInputException;
import model.Car;
import model.Driver;
import model.Trip;
import utils.DateParserUtils;
import utils.EntityFinderUtils;

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

    // Find other drivers who used the same vehicle on the same day (even partially)
    public String findOtherDrivers(String input) {
        StringBuilder result = new StringBuilder();

        if (!input.contains(";"))
            throw new InvalidInputException("Ungültiges Format. Erwartet: F003;2024-08-13");

        String[] parts = input.split(";", 2);
        String driverId = parts[0].trim();
        LocalDate date = DateParserUtils.parseDate(parts[1].trim());

        // Driver originalDriver = EntityFinderUtils.findDriverById(drivers, driverId);

        // Collect all cars used by the driver on that date (overlapping trips)
        Set<String> carIds = trips.stream()
                .filter(t -> t.driverId().equals(driverId) && t.overlapsWithDate(date))
                .map(Trip::carId)
                .collect(Collectors.toSet());

        if (carIds.isEmpty()) {
            return "Keine Fahrten für Fahrer %s am %s gefunden.\n".formatted(driverId, date);
        }

        Set<String> otherDriverEntries = new TreeSet<>();

        // Check other drivers using the same cars on the same day
        for (String carId : carIds) {
            List<Trip> sameDayTrips = trips.stream()
                    .filter(t -> t.carId().equals(carId)
                            && t.overlapsWithDate(date)
                            && !t.driverId().equals(driverId))
                    .toList();

            for (Trip trip : sameDayTrips) {
                Driver otherDriver = EntityFinderUtils.findDriverById(drivers, trip.driverId());
                Car car = EntityFinderUtils.findCarById(cars, carId);

                // otherDriverEntries.add("%s (IDs: %s, %s)"
                //        .formatted(otherDriver.getFullName(), otherDriver.id(), car.id()));
                otherDriverEntries.add("%s (%s)"
                        .formatted(otherDriver.getFullName(), car.licensePlate()));
            }
        }

        // Build result
        // result.append("\nFahrer hat etwas im Fahrzeug liegen lassen!\n");
        // result.append("Tag: ").append(date).append("\n");
        // result.append("Ursprünglicher Fahrer: ").append(originalDriver).append("\n");
        // result.append("Fahrzeuge an diesem Tag: ");
        // result.append(carIds.stream()
        //        .map(id -> cars.stream().filter(c -> c.id().equals(id)).findFirst().map(Car::toString).orElse("Unbekannt"))
        //        .collect(Collectors.joining(", "))).append("\n");

        if (otherDriverEntries.isEmpty()) {
            result.append("Keine anderen Fahrer gefunden.\n");
        } else {
        //    result.append("Andere Fahrer dieser Fahrzeuge:\n");
        //    otherDriverEntries.forEach(entry -> result.append("• ").append(entry).append("\n"));
              result.append(String.join(", ", otherDriverEntries));
        }

        return result.toString();
    }
}