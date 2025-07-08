package test.service;

import model.Driver;
import model.LicenseClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DriverSearchService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Tests für die Fahrersuche nach Namen.
 */
public class DriverSearchServiceTest {

    private DriverSearchService driverSearchService;

    @BeforeEach
    public void setUp() {
        Driver d1 = new Driver("F001", "Anna", "Musterfrau", LicenseClass.B);
        Driver d2 = new Driver("F002", "Max", "Beispiel", LicenseClass.C);
        Driver d3 = new Driver("F003", "Anna", "Beispiel", LicenseClass.A);
        driverSearchService = new DriverSearchService(List.of(d1, d2, d3));
    }

    @Test
    public void testSearchByNameFindsSingleMatch() {
        String result = driverSearchService.searchByName("muster");
        assertTrue(result.toLowerCase().contains("anna musterfrau"));
    }

    @Test
    public void testSearchByNameFindsMultipleMatches() {
        String result = driverSearchService.searchByName("anna");
        assertTrue(result.toLowerCase().contains("anna musterfrau"));
        assertTrue(result.toLowerCase().contains("anna beispiel"));
    }

    @Test
    public void testSearchByNameNoMatch() {
        String result = driverSearchService.searchByName("unbekannt");
        assertTrue(result.toLowerCase().contains("keine passenden fahrer gefunden"));
    }

    @Test
    public void testSearchByNameCaseInsensitive() {
        String result = driverSearchService.searchByName("MaX");
        assertTrue(result.toLowerCase().contains("max beispiel"));
    }
}