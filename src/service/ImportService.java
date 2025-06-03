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

    // Load all data from input file
    public void loadData(String filePath) {
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
                    // Delegate to parser based on entity type
                    switch (currentEntity) {
                        case "DRIVER" -> addDriver(parts, line);
                        case "CAR" -> addCar(parts, line);
                        case "TRIP" -> addTrip(parts, line);
                    }
                } catch (Exception e) {
                    // Skip malformed lines
                    System.err.println("Zeile übersprungen (Fehler: " + e.getMessage() + "): " + line);
                }
            }

            // Summary output
            System.out.println("Import abgeschlossen:");
            System.out.printf("• Fahrer:    %d%n", drivers.size());
            System.out.printf("• Fahrzeuge: %d%n", cars.size());
            System.out.printf("• Fahrten:   %d%n", trips.size());

        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Datei: " + e.getMessage());
        }
    }

    // Identify entity type by header structure
    private String detectEntityType(String header) {
        if (header.startsWith("fahrerId") && header.contains("startzeit")) return "TRIP";
        if (header.startsWith("fahrerId")) return "DRIVER";
        if (header.startsWith("fahrzeugId")) return "CAR";
        return "";
    }

    // Parse and add driver entry
    private void addDriver(String[] parts, String rawLine) {
        if (parts.length < 4) throw new IllegalArgumentException("Unvollständiger Fahrereintrag");
        String id = parts[0];
        if (drivers.stream().anyMatch(d -> d.id().equals(id)))
            throw new DuplicateEntityException("Fahrer mit ID " + id + " bereits vorhanden");
        drivers.add(new Driver(id, parts[1], parts[2], LicenseClass.fromString(parts[3])));
    }

    // Parse and add car entry
    private void addCar(String[] parts, String rawLine) {
        if (parts.length < 4) throw new IllegalArgumentException("Unvollständiger Fahrzeugeintrag");
        String id = parts[0];
        if (cars.stream().anyMatch(c -> c.id().equals(id)))
            throw new DuplicateEntityException("Fahrzeug mit ID " + id + " bereits vorhanden");
        cars.add(new Car(id, parts[1], parts[2], parts[3]));
    }

    // Parse and add trip entry
    private void addTrip(String[] parts, String rawLine) {
        if (parts.length < 6) throw new IllegalArgumentException("Unvollständiger Trip");

        String driverId = parts[0];
        String carId = parts[1];

        // Validate referenced driver and car
        if (drivers.stream().noneMatch(d -> d.id().equals(driverId)))
            throw new IllegalArgumentException("Unbekannte Fahrer-ID: " + driverId);
        if (cars.stream().noneMatch(c -> c.id().equals(carId)))
            throw new IllegalArgumentException("Unbekannte Fahrzeug-ID: " + carId);

        var startTime = parse(parts[4]);
        var endTime = parse(parts[5]);

        // Validate logical time order
        if (endTime.isBefore(startTime))
            throw new IllegalArgumentException("Endzeit liegt vor Startzeit");

        // Add valid trip
        trips.add(new Trip(
                driverId,
                carId,
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]),
                startTime,
                endTime
        ));
    }

    // Getters for imported entities
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