package service;

import model.Car;
import model.Driver;
import model.Trip;
import utils.DateParserUtils;
import utils.EntityFinderUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service zur Identifikation von Fahrern zu einem bestimmten Zeitpunkt auf Basis von Blitzerfotos o.ä.
 */
public class RadarTrapService {

    private final List<Driver> drivers;
    private final List<Car> cars;
    private final List<Trip> trips;

    /**
     * Initialisiert den Service mit allen vorhandenen Daten.
     *
     * @param drivers Liste aller Fahrer
     * @param cars    Liste aller Fahrzeuge
     * @param trips   Liste aller Fahrten
     */
    public RadarTrapService(List<Driver> drivers, List<Car> cars, List<Trip> trips) {
        this.drivers = drivers;
        this.cars = cars;
        this.trips = trips;
    }

    /**
     * Findet den Fahrer eines bestimmten Fahrzeugs zu einem gegebenen Zeitpunkt.
     *
     * @param input Argument im Format "Kennzeichen;Zeit" (z. B. "S-AB-1234;2024-01-01T10:00:00")
     * @return Gefundener Fahrername oder Fehlermeldung
     * @throws IllegalArgumentException bei falschem Format
     */
    public String findDriverAtTime(String input) {
        StringBuilder result = new StringBuilder();

        if (!input.contains(";"))
            throw new IllegalArgumentException("Ungültiges Format. Erwartet: S-XX-1234;2024-02-14T13:57:43");

        String[] parts = input.split(";", 2);
        String licensePlate = parts[0].trim();
        LocalDateTime timestamp = DateParserUtils.parseDateTime(parts[1].trim());

        Car car = EntityFinderUtils.findCarByLicensePlate(cars, licensePlate);

        // Finde alle Fahrten mit diesem Fahrzeug zur gegebenen Zeit
        List<Trip> matchingTrips = trips.stream()
                .filter(t -> t.carId().equals(car.id()) && t.includesTime(timestamp))
                .toList();

        if (matchingTrips.isEmpty()) {
            return "Kein Fahrer gefunden.\n";
        }

        for (Trip trip : matchingTrips) {
            Driver driver = EntityFinderUtils.findDriverById(drivers, trip.driverId());
            result.append("Fahrer: ").append(driver.getFullName()).append("\n");
        }

        return result.toString();
    }
}