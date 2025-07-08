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

/**
 * Service zum Auffinden von Fahrern, die dasselbe Fahrzeug am selben Tag verwendet haben.
 */
public class LostAndFoundService {

    private final List<Driver> drivers;
    private final List<Car> cars;
    private final List<Trip> trips;

    /**
     * Erstellt den Service mit bekannten Fahrern, Fahrzeugen und Fahrten.
     *
     * @param drivers Liste aller Fahrer
     * @param cars    Liste aller Fahrzeuge
     * @param trips   Liste aller Fahrten
     */
    public LostAndFoundService(List<Driver> drivers, List<Car> cars, List<Trip> trips) {
        this.drivers = drivers;
        this.cars = cars;
        this.trips = trips;
    }

    /**
     * Findet andere Fahrer, die am selben Tag dasselbe Fahrzeug genutzt haben.
     *
     * @param input Eingabe im Format "FahrerID;Datum" (z. B. "F003;2024-08-13")
     * @return String mit gefundener Fahrer-Liste oder Hinweis auf keine Übereinstimmungen
     * @throws InvalidInputException bei falschem Eingabeformat
     */
    public String findOtherDrivers(String input) {
        StringBuilder result = new StringBuilder();

        if (!input.contains(";"))
            throw new InvalidInputException("Ungültiges Format. Erwartet: F003;2024-08-13");

        String[] parts = input.split(";", 2);
        String driverId = parts[0].trim();
        LocalDate date = DateParserUtils.parseDate(parts[1].trim());

        // Alle Fahrzeuge, die der Fahrer an diesem Tag genutzt hat
        Set<String> carIds = trips.stream()
                .filter(t -> t.driverId().equals(driverId) && t.overlapsWithDate(date))
                .map(Trip::carId)
                .collect(Collectors.toSet());

        if (carIds.isEmpty()) {
            return "Keine Fahrten für Fahrer %s am %s gefunden.\n".formatted(driverId, date);
        }

        Set<String> otherDriverEntries = new TreeSet<>();

        // Finde weitere Fahrer derselben Fahrzeuge am selben Tag
        for (String carId : carIds) {
            List<Trip> sameDayTrips = trips.stream()
                    .filter(t -> t.carId().equals(carId)
                            && t.overlapsWithDate(date)
                            && !t.driverId().equals(driverId))
                    .toList();

            for (Trip trip : sameDayTrips) {
                Driver otherDriver = EntityFinderUtils.findDriverById(drivers, trip.driverId());
                Car car = EntityFinderUtils.findCarById(cars, carId);

                // Ausgabe enthält Fahrername + Kennzeichen
                otherDriverEntries.add("%s (%s)"
                        .formatted(otherDriver.getFullName(), car.licensePlate()));
            }
        }

        if (otherDriverEntries.isEmpty()) {
            result.append("Keine anderen Fahrer gefunden.\n");
        } else {
            result.append(String.join(", ", otherDriverEntries));
        }

        return result.toString();
    }
}