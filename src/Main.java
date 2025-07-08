import exceptions.EntityNotFoundException;
import exceptions.InvalidInputException;
import model.*;
import service.*;

import java.util.List;

/**
 * Hauptklasse des Programms.
 * Verantwortlich für das Laden von Daten, Initialisieren der Services und Verarbeiten der Befehle.
 */
public class Main {
    /**
     * Einstiegspunkt der Anwendung.
     *
     * @param args Kommandozeilenargumente zur Steuerung des Programmablaufs
     */
    public static void main(String[] args) {
        try {
            // Initialisiere Import-Service und lade Daten
            ImportService importService = new ImportService();
            importService.loadData("src/dienstwagenprojekt2025.db");

            // Extrahiere importierte Entitäten
            List<Driver> drivers = importService.getDrivers();
            List<Car> cars = importService.getCars();
            List<Trip> trips = importService.getTrips();

            // Initialisiere Services für verschiedene Funktionen
            RadarTrapService radarTrapService = new RadarTrapService(drivers, cars, trips);
            LostAndFoundService lostAndFoundService = new LostAndFoundService(drivers, cars, trips);
            DriverSearchService driverSearchService = new DriverSearchService(drivers);
            CarSearchService carSearchService = new CarSearchService(cars);

            // Befehlsverarbeitung über zentrale Handlerklasse
            CommandHandler commandHandler = new CommandHandler(
                    radarTrapService,
                    lostAndFoundService,
                    driverSearchService,
                    carSearchService
            );

            // Verarbeite alle übergebenen Argumente
            for (String arg : args) {
                try {
                    commandHandler.handle(arg);
                } catch (InvalidInputException | EntityNotFoundException e) {
                    System.out.println("Fehler: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Interner Fehler: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("Anwendungsfehler beim Start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}