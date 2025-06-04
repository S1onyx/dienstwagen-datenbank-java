import exceptions.EntityNotFoundException;
import exceptions.InvalidInputException;
import model.Car;
import model.Driver;
import service.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Load data from file
            ImportService importService = new ImportService();
            importService.loadData("src/dienstwagenprojekt2025.db");

            // Extract imported entities
            List<Driver> drivers = importService.getDrivers();
            List<Car> cars = importService.getCars();
            var trips = importService.getTrips();

            // Initialize services with data
            RadarTrapService radarTrapService = new RadarTrapService(drivers, cars, trips);
            LostAndFoundService lostAndFoundService = new LostAndFoundService(drivers, cars, trips);
            DriverSearchService driverSearchService = new DriverSearchService(drivers);
            CarSearchService carSearchService = new CarSearchService(cars);
            CommandHandler commandHandler = new CommandHandler(
                    radarTrapService,
                    lostAndFoundService,
                    driverSearchService,
                    carSearchService
            );

            // Process each command line argument
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