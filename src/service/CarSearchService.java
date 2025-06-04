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
        cars.stream()
                .filter(c -> (c.manufacturer() + c.model() + c.licensePlate()).toLowerCase().contains(search))
                .forEach(System.out::println);
    }
}