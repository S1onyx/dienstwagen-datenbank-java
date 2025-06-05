package service;

import model.Driver;

import java.util.List;

public class DriverSearchService {
    private final List<Driver> drivers;

    public DriverSearchService(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public void searchByName(String keyword) {
        String search = keyword.toLowerCase();
        System.out.println("\nSuchergebnisse für Fahrer mit Schlüsselwort '" + keyword + "':");

        List<Driver> matches = drivers.stream()
                .filter(d -> (d.firstName() + " " + d.lastName()).toLowerCase().contains(search))
                .toList();

        if (matches.isEmpty()) {
            System.out.println("Keine passenden Fahrer gefunden.");
        } else {
            matches.forEach(System.out::println);
        }
    }
}