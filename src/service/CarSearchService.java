package service;

import model.Car;

import java.util.List;

public class CarSearchService {
    private final List<Car> cars;

    public CarSearchService(List<Car> cars) {
        this.cars = cars;
    }

    public void searchByKeyword(String keyword) {
        String search = keyword.toLowerCase();
        System.out.println("\nSuchergebnisse für Fahrzeuge mit Schlüsselwort '" + keyword + "':");

        List<Car> matches = cars.stream()
                .filter(c -> (c.manufacturer() + c.model() + c.licensePlate()).toLowerCase().contains(search))
                .toList();

        if (matches.isEmpty()) {
            System.out.println("Keine passenden Fahrzeuge gefunden.");
        } else {
            matches.forEach(System.out::println);
        }
    }
}