package service;

import model.Driver;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviceklasse zur Suche nach Fahrern anhand von Namen.
 */
public class DriverSearchService {
    private final List<Driver> drivers;

    /**
     * Erstellt den Service mit der gegebenen Fahrerliste.
     *
     * @param drivers Liste aller bekannten Fahrer
     */
    public DriverSearchService(List<Driver> drivers) {
        this.drivers = drivers;
    }

    /**
     * Sucht nach Fahrern, deren vollständiger Name den Suchbegriff enthält.
     *
     * @param keyword Suchbegriff (case-insensitiv)
     * @return Formatierte Ergebnisliste oder Hinweis bei keiner Übereinstimmung
     */
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