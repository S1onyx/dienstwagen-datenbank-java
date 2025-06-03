package service;

import model.Car;
import model.Driver;
import model.Trip;

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

    public void findDriverAtTime(String input) {
        if (!input.contains(";")) {
            System.out.println("Ungültiges Format. Erwartet: S-XX-1234;2024-02-14T13:57:43");
            return;
        }

        String[] parts = input.split(";", 2);
        String licensePlate = parts[0].trim();
        String timeString = parts[1].trim();

        LocalDateTime timestamp;
        try {
            timestamp = LocalDateTime.parse(timeString);
        } catch (Exception e) {
            System.out.println("Fehler: Datum muss im Format yyyy-MM-ddTHH:mm:ss angegeben sein.");
            return;
        }

        // passendes Fahrzeug suchen
        List<Car> fahrzeuge = cars.stream()
                .filter(car -> car.getLicensePlate().equalsIgnoreCase(licensePlate))
                .toList();

        if (fahrzeuge.isEmpty()) {
            System.out.println("Kein Fahrzeug mit dem Kennzeichen \"" + licensePlate + "\" gefunden.");
            return;
        }

        boolean hasAnyMatch = false;

        for (Car car : fahrzeuge) {
            List<Trip> passendeFahrten = trips.stream()
                    .filter(trip -> trip.getCarId().equals(car.getId()) &&
                            !timestamp.isBefore(trip.getStartTime()) &&
                            !timestamp.isAfter(trip.getEndTime()))
                    .toList();

            if (!passendeFahrten.isEmpty()) {
                hasAnyMatch = true;
                for (Trip trip : passendeFahrten) {
                    Driver driver = drivers.stream()
                            .filter(d -> d.getId().equals(trip.getDriverId()))
                            .findFirst()
                            .orElse(null);
                    if (driver != null) {
                        System.out.println("\nGeblitzt während folgender Fahrt:");
                        System.out.println("Fahrzeug: " + car);
                        System.out.println("Fahrt: " + trip);
                        System.out.println("Fahrer: " + driver.getFirstName() + " " + driver.getLastName()
                                + " (" + driver.getId() + ", Klasse " + driver.getLicenseClass() + ")");
                    }
                }
            }
        }

        if (!hasAnyMatch) {
            System.out.println("Kein Fahrer gefunden.");
        }
    }
}