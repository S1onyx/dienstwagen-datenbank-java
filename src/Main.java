import model.Driver;
import model.Car;
import model.Trip;
import service.ImportService;
import service.RadarTrapService;
import service.LostAndFoundService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ImportService importService = new ImportService();
        importService.loadData("src/dienstwagenprojekt2025.db");

        List<Driver> drivers = importService.getDrivers();
        List<Car> cars = importService.getCars();
        List<Trip> trips = importService.getTrips();

        RadarTrapService radarTrapService = new RadarTrapService(drivers, cars, trips);
        LostAndFoundService lostAndFoundService = new LostAndFoundService(drivers, cars, trips);

        for (String arg : args) {
            if (arg.startsWith("--fahrersuche=")) {
                String keyword = arg.split("=", 2)[1].trim().toLowerCase();
                System.out.println("\nSuchergebnisse für Fahrer mit Schlüsselwort '" + keyword + "':");
                drivers.stream()
                        .filter(driver -> (driver.getFirstName() + " " + driver.getLastName()).toLowerCase().contains(keyword))
                        .forEach(System.out::println);
            } else if (arg.startsWith("--fahrzeugsuche=")) {
                String keyword = arg.split("=", 2)[1].trim().toLowerCase();
                System.out.println("\nSuchergebnisse für Fahrzeuge mit Schlüsselwort '" + keyword + "':");
                cars.stream()
                        .filter(car -> (car.getManufacturer() + car.getModel() + car.getLicensePlate()).toLowerCase().contains(keyword))
                        .forEach(System.out::println);
            } else if (arg.startsWith("--fahrerZeitpunkt=")) {
                String input = arg.split("=", 2)[1].trim();
                radarTrapService.findDriverAtTime(input);
            } else if (arg.startsWith("--fahrerDatum=")) {
                String input = arg.split("=", 2)[1].trim();
                lostAndFoundService.findOtherDrivers(input);
            } else if(arg.startsWith("--help")) {
                System.out.println(arg);
                System.out.println("Verfügbare Optionen:");
                System.out.println("--fahrersuche=<Suchbegriff> : Suche nach Fahrern mit dem angegebenen Suchbegriff.");
                System.out.println("--fahrzeugsuche=<Suchbegriff> : Suche nach Fahrzeugen mit dem angegebenen Suchbegriff.");
                System.out.println("--fahrerZeitpunkt=<Kennzeichen>;yyyy-MM-ddTHH:mm:ss : Finde Fahrer zum angegebenen Zeitpunkt für das Fahrzeug mit dem Kennzeichen.");
                System.out.println("--fahrerDatum=<Fahrer-ID>;yyyy-MM-dd : Finde andere Fahrer, die am angegebenen Datum dasselbe Fahrzeug gefahren haben.");
            } else {
                System.out.println("Unbekannte Option: " + arg);
            }
        }
    }
}