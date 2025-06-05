package service;

import model.Driver;

import java.util.List;
import java.util.stream.Collectors;

public class DriverSearchService {
    private final List<Driver> drivers;

    public DriverSearchService(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public String searchByName(String keyword) {
        StringBuilder result = new StringBuilder();
        String search = keyword.toLowerCase();
        result.append("\nSuchergebnisse für Fahrer mit Schlüsselwort '").append(keyword).append("':\n");

        List<Driver> matches = drivers.stream()
                .filter(d -> (d.firstName() + " " + d.lastName()).toLowerCase().contains(search))
                .toList();

        if (matches.isEmpty()) {
            result.append("Keine passenden Fahrer gefunden.\n");
        } else {
            result.append(matches.stream().map(Driver::toString).collect(Collectors.joining("\n"))).append("\n");
        }

        return result.toString();
    }
}