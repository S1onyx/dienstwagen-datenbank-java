package service;

import model.Car;

import java.util.List;
import java.util.stream.Collectors;

public class CarSearchService {
    private final List<Car> cars;

    public CarSearchService(List<Car> cars) {
        this.cars = cars;
    }

    public String searchByKeyword(String keyword) {
        StringBuilder result = new StringBuilder();
        String search = keyword.toLowerCase();
        result.append("\nSuchergebnisse für Fahrzeuge mit Schlüsselwort '").append(keyword).append("':\n");

        List<Car> matches = cars.stream()
                .filter(c -> (c.manufacturer() + c.model() + c.licensePlate()).toLowerCase().contains(search))
                .toList();

        if (matches.isEmpty()) {
            result.append("Keine passenden Fahrzeuge gefunden.\n");
        } else {
            result.append(matches.stream().map(Car::toString).collect(Collectors.joining("\n"))).append("\n");
        }

        return result.toString();
    }
}