package test.service;

import model.Driver;
import model.LicenseClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.DriverSearchService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DriverSearchServiceTest {

    private DriverSearchService driverSearchService;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        Driver d1 = new Driver("F001", "Anna", "Musterfrau", LicenseClass.B);
        Driver d2 = new Driver("F002", "Max", "Beispiel", LicenseClass.C);
        Driver d3 = new Driver("F003", "Anna", "Beispiel", LicenseClass.A);
        driverSearchService = new DriverSearchService(List.of(d1, d2, d3));
        System.setOut(new PrintStream(output));
    }

    @Test
    public void testSearchByNameFindsSingleMatch() {
        driverSearchService.searchByName("muster");
        assertTrue(output.toString().toLowerCase().contains("anna musterfrau"));
    }

    @Test
    public void testSearchByNameFindsMultipleMatches() {
        driverSearchService.searchByName("anna");
        String result = output.toString().toLowerCase();
        assertTrue(result.contains("anna musterfrau") && result.contains("anna beispiel"));
    }

    @Test
    public void testSearchByNameNoMatch() {
        driverSearchService.searchByName("unbekannt");
        assertTrue(output.toString().toLowerCase().contains("keine passenden fahrer gefunden"));
    }

    @Test
    public void testSearchByNameCaseInsensitive() {
        driverSearchService.searchByName("MaX");
        assertTrue(output.toString().toLowerCase().contains("max beispiel"));
    }
}