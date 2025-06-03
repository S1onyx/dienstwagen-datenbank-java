package service;

import model.Car;
import model.Driver;
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

    public void loadData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentEntity = "";

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("New_Entity:")) {
                    String header = line.substring("New_Entity:".length()).trim();
                    currentEntity = detectEntityType(header);
                    continue;
                }

                String[] parts = line.split(",");
                for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();

                try {
                    switch (currentEntity) {
                        case "DRIVER" -> addDriver(parts, line);
                        case "CAR" -> addCar(parts, line);
                        case "TRIP" -> addTrip(parts, line);
                    }
                } catch (Exception e) {
                    System.err.println("Zeile übersprungen (Fehler: " + e.getMessage() + "): " + line);
                }
            }

            System.out.println("Import abgeschlossen:");
            System.out.printf("• Fahrer:    %d%n", drivers.size());
            System.out.printf("• Fahrzeuge: %d%n", cars.size());
            System.out.printf("• Fahrten:   %d%n", trips.size());

        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Datei: " + e.getMessage());
        }
    }

    private String detectEntityType(String header) {
        if (header.startsWith("fahrerId") && header.contains("startzeit")) return "TRIP";
        if (header.startsWith("fahrerId")) return "DRIVER";
        if (header.startsWith("fahrzeugId")) return "CAR";
        return "";
    }

    private void addDriver(String[] parts, String rawLine) {
        if (parts.length < 4) throw new IllegalArgumentException("Unvollständiger Fahrereintrag");
        String id = parts[0];
        if (drivers.stream().anyMatch(d -> d.id().equals(id)))
            throw new IllegalStateException("Fahrer mit ID " + id + " bereits vorhanden");
        drivers.add(new Driver(id, parts[1], parts[2], parts[3]));
    }

    private void addCar(String[] parts, String rawLine) {
        if (parts.length < 4) throw new IllegalArgumentException("Unvollständiger Fahrzeugeintrag");
        String id = parts[0];
        if (cars.stream().anyMatch(c -> c.id().equals(id)))
            throw new IllegalStateException("Fahrzeug mit ID " + id + " bereits vorhanden");
        cars.add(new Car(id, parts[1], parts[2], parts[3]));
    }

    private void addTrip(String[] parts, String rawLine) {
        if (parts.length < 6) throw new IllegalArgumentException("Unvollständiger Trip");
        trips.add(new Trip(
                parts[0],
                parts[1],
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]),
                parse(parts[4]),
                parse(parts[5])
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