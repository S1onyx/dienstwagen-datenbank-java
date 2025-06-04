package test.service;

import model.Driver;
import model.LicenseClass;
import org.junit.jupiter.api.Test;
import service.DriverSearchService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DriverSearchServiceTest {

    @Test
    void testSearchByNameFindsMatch() {
        Driver d1 = new Driver("F001", "Anna", "Musterfrau", LicenseClass.B);
        Driver d2 = new Driver("F002", "Max", "Beispiel", LicenseClass.B);
        DriverSearchService service = new DriverSearchService(List.of(d1, d2));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        service.searchByName("anna");

        String result = output.toString().toLowerCase();
        assertTrue(result.contains("anna musterfrau"));
    }
}