package service;

import model.Car;
import model.Driver;
import model.Trip;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
                if (line.startsWith("New_Entity:")) {
                    String header = line.substring("New_Entity:".length()).trim();
                    if (header.startsWith("fahrerId") && header.contains("startzeit")) {
                        currentEntity = "TRIP";
                    } else if (header.startsWith("fahrerId")) {
                        currentEntity = "DRIVER";
                    } else if (header.startsWith("fahrzeugId")) {
                        currentEntity = "CAR";
                    }
                    continue;
                }

                String[] parts = line.split(",");
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }

                switch (currentEntity) {
                    case "DRIVER":
                        if (parts.length >= 4) {
                            String id = parts[0];
                            if (drivers.stream().noneMatch(d -> d.getId().equals(id))) {
                                drivers.add(new Driver(id, parts[1], parts[2], parts[3]));
                            }
                            else {
                                System.err.println("Fahrer mit ID " + id + " bereits vorhanden, " +
                                        "überspringe Eintrag: " + line);
                            }
                        }
                        break;
                    case "CAR":
                        if (parts.length >= 4) {
                            String id = parts[0];
                            if (cars.stream().noneMatch(c -> c.getId().equals(id))) {
                                cars.add(new Car(id, parts[1], parts[2], parts[3]));
                            } else {
                                System.err.println("Fahrzeug mit ID " + id + " bereits vorhanden, " +
                                        "überspringe Eintrag: " + line);
                            }
                        }
                        break;
                    case "TRIP":
                        if (parts.length >= 6) {
                            trips.add(new Trip(
                                    parts[0],
                                    parts[1],
                                    Integer.parseInt(parts[2]),
                                    Integer.parseInt(parts[3]),
                                    LocalDateTime.parse(parts[4]),
                                    LocalDateTime.parse(parts[5])
                            ));
                        } else {
                            System.err.println("Ungültige Trip-Daten, überspringe Eintrag: " + line);
                        }
                        break;
                }
            }

            System.out.println("Import abgeschlossen:");
            System.out.println("Eingelesene Fahrer: " + drivers.size());
            System.out.println("Eingelesene Fahrzeuge: " + cars.size());
            System.out.println("Eingelesene Fahrten: " + trips.size());

        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Datei: " + e.getMessage());
        }
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