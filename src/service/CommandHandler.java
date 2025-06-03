package service;

import model.Car;
import model.Driver;
import utils.ArgParserUtils;

import java.util.List;

public class CommandHandler {
    private final List<Driver> drivers;
    private final List<Car> cars;
    private final RadarTrapService radarTrapService;
    private final LostAndFoundService lostAndFoundService;

    public CommandHandler(List<Driver> drivers, List<Car> cars,
                          RadarTrapService radarTrapService, LostAndFoundService lostAndFoundService) {
        this.drivers = drivers;
        this.cars = cars;
        this.radarTrapService = radarTrapService;
        this.lostAndFoundService = lostAndFoundService;
    }

    public void handle(String arg) {
        if (arg.startsWith("--fahrersuche=")) {
            handleFahrersuche(arg);
        } else if (arg.startsWith("--fahrzeugsuche=")) {
            handleFahrzeugsuche(arg);
        } else if (arg.startsWith("--fahrerZeitpunkt=")) {
            radarTrapService.findDriverAtTime(ArgParserUtils.extractValue(arg));
        } else if (arg.startsWith("--fahrerDatum=")) {
            lostAndFoundService.findOtherDrivers(ArgParserUtils.extractValue(arg));
        } else if (arg.equals("--help") || arg.isEmpty()) {
            printHelp();
        } else {
            System.out.println("Unbekannte Option: " + arg);
        }
    }

    private void handleFahrersuche(String arg) {
        String keyword = ArgParserUtils.extractValue(arg).toLowerCase();
        System.out.println("\nSuchergebnisse für Fahrer mit Schlüsselwort '" + keyword + "':");
        drivers.stream()
                .filter(d -> (d.firstName() + " " + d.lastName()).toLowerCase().contains(keyword))
                .forEach(System.out::println);
    }

    private void handleFahrzeugsuche(String arg) {
        String keyword = ArgParserUtils.extractValue(arg).toLowerCase();
        System.out.println("\nSuchergebnisse für Fahrzeuge mit Schlüsselwort '" + keyword + "':");
        cars.stream()
                .filter(c -> (c.manufacturer() + c.model() + c.licensePlate()).toLowerCase().contains(keyword))
                .forEach(System.out::println);
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