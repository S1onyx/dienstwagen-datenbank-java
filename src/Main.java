import exceptions.EntityNotFoundException;
import exceptions.InvalidInputException;
import model.Car;
import model.Driver;
import service.ImportService;
import service.LostAndFoundService;
import service.RadarTrapService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            ImportService importService = new ImportService();
            importService.loadData("src/dienstwagenprojekt2025.db");

            List<Driver> drivers = importService.getDrivers();
            List<Car> cars = importService.getCars();
            var trips = importService.getTrips();

            RadarTrapService radarTrapService = new RadarTrapService(drivers, cars, trips);
            LostAndFoundService lostAndFoundService = new LostAndFoundService(drivers, cars, trips);

            if (args.length == 0) {
                printHelp();
                return;
            }

            for (String arg : args) {
                try {
                    if (arg.startsWith("--fahrersuche=")) {
                        handleFahrersuche(arg, drivers);
                    } else if (arg.startsWith("--fahrzeugsuche=")) {
                        handleFahrzeugsuche(arg, cars);
                    } else if (arg.startsWith("--fahrerZeitpunkt=")) {
                        radarTrapService.findDriverAtTime(extractValue(arg));
                    } else if (arg.startsWith("--fahrerDatum=")) {
                        lostAndFoundService.findOtherDrivers(extractValue(arg));
                    } else if (arg.equals("--help")) {
                        printHelp();
                    } else {
                        System.out.println("Unbekannte Option: " + arg);
                    }
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

    private static void handleFahrersuche(String arg, List<Driver> drivers) {
        String keyword = extractValue(arg).toLowerCase();
        System.out.println("\nSuchergebnisse für Fahrer mit Schlüsselwort '" + keyword + "':");
        drivers.stream()
                .filter(d -> (d.firstName() + " " + d.lastName()).toLowerCase().contains(keyword))
                .forEach(System.out::println);
    }

    private static void handleFahrzeugsuche(String arg, List<Car> cars) {
        String keyword = extractValue(arg).toLowerCase();
        System.out.println("\nSuchergebnisse für Fahrzeuge mit Schlüsselwort '" + keyword + "':");
        cars.stream()
                .filter(c -> (c.manufacturer() + c.model() + c.licensePlate()).toLowerCase().contains(keyword))
                .forEach(System.out::println);
    }

    private static String extractValue(String arg) {
        return arg.split("=", 2)[1].trim();
    }

    private static void printHelp() {
        System.out.println("Verfügbare Optionen:");
        System.out.println("--fahrersuche=<Suchbegriff>        : Suche nach Fahrern mit dem angegebenen Suchbegriff.");
        System.out.println("--fahrzeugsuche=<Suchbegriff>      : Suche nach Fahrzeugen mit dem angegebenen Suchbegriff.");
        System.out.println("--fahrerZeitpunkt=<Kennz.;Zeit>    : Finde Fahrer zum Zeitpunkt (Format: S-XX-1234;2024-01-01T14:00:00).");
        System.out.println("--fahrerDatum=<FahrerID;Datum>     : Finde andere Fahrer desselben Fahrzeugs am Tag (Format: F001;2024-01-01).");
        System.out.println("--help                              : Zeige diese Hilfe an.");
    }
}