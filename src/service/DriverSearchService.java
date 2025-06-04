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
        drivers.stream()
                .filter(d -> (d.firstName() + " " + d.lastName()).toLowerCase().contains(search))
                .forEach(System.out::println);
    }
}