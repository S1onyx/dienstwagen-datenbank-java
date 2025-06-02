import model.Driver;
import model.Car;
import model.Trip;
import service.ImportService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ImportService importService = new ImportService();
        importService.loadData("src/dienstwagenprojekt2025.db");

        List<Driver> drivers = importService.getDrivers();
        List<Car> cars = importService.getCars();
        List<Trip> trips = importService.getTrips();

        System.out.println("\nFahrer:");
        drivers.stream().limit(10).forEach(System.out::println);

        System.out.println("\nFahrzeuge:");
        cars.stream().limit(10).forEach(System.out::println);

        System.out.println("\nFahrten:");
        trips.stream().limit(10).forEach(System.out::println);

        for (String arg : args) {
            if (arg.startsWith("--fahrersuche=")) {
                String keyword = arg.split("=", 2)[1].trim().toLowerCase();
                drivers.stream()
                        .filter(driver -> (driver.getFirstName() + " " + driver.getLastName()).toLowerCase().contains(keyword))
                        .forEach(System.out::println);
            } else if (arg.startsWith("--fahrzeugsuche=")) {
                String keyword = arg.split("=", 2)[1].trim().toLowerCase();
                cars.stream()
                        .filter(car -> (car.getManufacturer() + car.getModel() + car.getLicensePlate()).toLowerCase().contains(keyword))
                        .forEach(System.out::println);
            }
        }
    }
}