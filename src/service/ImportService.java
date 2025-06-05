package service;

import exceptions.DuplicateEntityException;
import model.Car;
import model.Driver;
import model.LicenseClass;
import model.Trip;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.parse;

public class ImportService {

    private final List<Driver> drivers = new ArrayList<>();
    private final List<Car> cars = new ArrayList<>();
    private final List<Trip> trips = new ArrayList<>();

    // Load all data from input file and return summary
    public String loadData(String filePath) {
        StringBuilder result = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentEntity = "";

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Check for entity type declaration
                if (line.startsWith("New_Entity:")) {
                    String header = line.substring("New_Entity:".length()).trim();
                    currentEntity = detectEntityType(header);
                    continue;
                }

                String[] parts = line.split(",");
                for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();

                try {
                    switch (currentEntity) {
                        case "DRIVER" -> addDriver(parts);
                        case "CAR" -> addCar(parts);
                        case "TRIP" -> addTrip(parts);
                    }
                } catch (Exception e) {
                    result.append("Zeile übersprungen (Fehler: ").append(e.getMessage()).append("): ").append(line).append("\n");
                }
            }

            result.append("Import abgeschlossen:\n");
            result.append("• Fahrer:    ").append(drivers.size()).append("\n");
            result.append("• Fahrzeuge: ").append(cars.size()).append("\n");
            result.append("• Fahrten:   ").append(trips.size()).append("\n");

        } catch (Exception e) {
            result.append("Fehler beim Laden der Datei: ").append(e.getMessage()).append("\n");
        }

        return result.toString();
    }

    private String detectEntityType(String header) {
        if (header.startsWith("fahrerId") && header.contains("startzeit")) return "TRIP";
        if (header.startsWith("fahrerId")) return "DRIVER";
        if (header.startsWith("fahrzeugId")) return "CAR";
        return "";
    }

    private void addDriver(String[] parts) {
        if (parts.length < 4) throw new IllegalArgumentException("Unvollständiger Fahrereintrag");
        String id = parts[0];
        if (drivers.stream().anyMatch(d -> d.id().equals(id)))
            throw new DuplicateEntityException("Fahrer mit ID " + id + " bereits vorhanden");
        drivers.add(new Driver(id, parts[1], parts[2], LicenseClass.fromString(parts[3])));
    }

    private void addCar(String[] parts) {
        if (parts.length < 4) throw new IllegalArgumentException("Unvollständiger Fahrzeugeintrag");
        String id = parts[0];
        if (cars.stream().anyMatch(c -> c.id().equals(id)))
            throw new DuplicateEntityException("Fahrzeug mit ID " + id + " bereits vorhanden");
        cars.add(new Car(id, parts[1], parts[2], parts[3]));
    }

    private void addTrip(String[] parts) {
        if (parts.length < 6) throw new IllegalArgumentException("Unvollständiger Trip");

        String driverId = parts[0];
        String carId = parts[1];

        if (drivers.stream().noneMatch(d -> d.id().equals(driverId)))
            throw new IllegalArgumentException("Unbekannte Fahrer-ID: " + driverId);
        if (cars.stream().noneMatch(c -> c.id().equals(carId)))
            throw new IllegalArgumentException("Unbekannte Fahrzeug-ID: " + carId);

        var startTime = parse(parts[4]);
        var endTime = parse(parts[5]);

        if (endTime.isBefore(startTime))
            throw new IllegalArgumentException("Endzeit liegt vor Startzeit");

        trips.add(new Trip(
                driverId,
                carId,
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]),
                startTime,
                endTime
        ));
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public List<Car> getCars() {
        return cars;
    }

    public List<Trip> getTrips() {
        return trips;
    }
}