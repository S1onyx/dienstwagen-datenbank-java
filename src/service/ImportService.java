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

/**
 * Serviceklasse zum Importieren von Fahrern, Fahrzeugen und Fahrten aus einer Datendatei.
 */
public class ImportService {

    private final List<Driver> drivers = new ArrayList<>();
    private final List<Car> cars = new ArrayList<>();
    private final List<Trip> trips = new ArrayList<>();

    /**
     * Lädt alle Daten aus der angegebenen Datei und speichert sie intern.
     *
     * @param filePath Pfad zur Datenbankdatei
     * @return Zusammenfassung des Imports als Text
     */
    public String loadData(String filePath) {
        StringBuilder result = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentEntity = "";

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Abschnittserkennung (z. B. New_Entity: fahrerId,...)
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

    /**
     * Erkennt den Entitätstyp anhand der Headerzeile.
     *
     * @param header erste Zeile nach "New_Entity:"
     * @return "DRIVER", "CAR" oder "TRIP"
     */
    private String detectEntityType(String header) {
        if (header.startsWith("fahrerId") && header.contains("startzeit")) return "TRIP";
        if (header.startsWith("fahrerId")) return "DRIVER";
        if (header.startsWith("fahrzeugId")) return "CAR";
        return "";
    }

    /**
     * Fügt einen Fahrer hinzu, sofern noch nicht vorhanden.
     *
     * @param parts Zeile als Array [id, vorname, nachname, klasse]
     */
    private void addDriver(String[] parts) {
        if (parts.length < 4) throw new IllegalArgumentException("Unvollständiger Fahrereintrag");
        String id = parts[0];
        if (drivers.stream().anyMatch(d -> d.id().equals(id)))
            throw new DuplicateEntityException("Fahrer mit ID " + id + " bereits vorhanden");
        drivers.add(new Driver(id, parts[1], parts[2], LicenseClass.fromString(parts[3])));
    }

    /**
     * Fügt ein Fahrzeug hinzu, sofern noch nicht vorhanden.
     *
     * @param parts Zeile als Array [id, hersteller, modell, kennzeichen]
     */
    private void addCar(String[] parts) {
        if (parts.length < 4) throw new IllegalArgumentException("Unvollständiger Fahrzeugeintrag");
        String id = parts[0];
        if (cars.stream().anyMatch(c -> c.id().equals(id)))
            throw new DuplicateEntityException("Fahrzeug mit ID " + id + " bereits vorhanden");
        cars.add(new Car(id, parts[1], parts[2], parts[3]));
    }

    /**
     * Fügt eine Fahrt hinzu, sofern alle IDs gültig sind und Zeiten korrekt sind.
     *
     * @param parts Zeile als Array [fahrerId, fahrzeugId, startKm, endKm, startzeit, endzeit]
     */
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

    /**
     * @return Liste aller importierten Fahrer
     */
    public List<Driver> getDrivers() {
        return drivers;
    }

    /**
     * @return Liste aller importierten Fahrzeuge
     */
    public List<Car> getCars() {
        return cars;
    }

    /**
     * @return Liste aller importierten Fahrten
     */
    public List<Trip> getTrips() {
        return trips;
    }
}