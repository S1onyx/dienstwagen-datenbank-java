package test.service;

import model.Car;
import model.Driver;
import model.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.RadarTrapService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für die Blitzer-Funktion (Fahrer zum bestimmten Zeitpunkt).
 */
public class RadarTrapServiceTest {

    private RadarTrapService radarTrapService;

    @BeforeEach
    public void setUp() {
        Driver driver = new Driver("F001", "Anna", "Muster", model.LicenseClass.B);
        Car car = new Car("C001", "BMW", "320i", "S-XX-1234");

        Trip trip = new Trip("F001", "C001", 10000, 10200,
                LocalDateTime.parse("2024-01-01T10:00:00"),
                LocalDateTime.parse("2024-01-01T11:00:00"));

        radarTrapService = new RadarTrapService(List.of(driver), List.of(car), List.of(trip));
    }

    @Test
    public void testFindDriverAtTimeSuccess() {
        String result = radarTrapService.findDriverAtTime("S-XX-1234;2024-01-01T10:30:00");
        assertTrue(result.contains("Fahrer:"));
    }

    @Test
    public void testFindDriverAtTimeNoMatch() {
        String result = radarTrapService.findDriverAtTime("S-XX-1234;2024-01-01T12:00:00");
        assertTrue(result.contains("Kein Fahrer gefunden."));
    }

    @Test
    public void testInvalidFormatThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                radarTrapService.findDriverAtTime("S-XX-1234 2024-01-01T10:00:00"));
    }
}