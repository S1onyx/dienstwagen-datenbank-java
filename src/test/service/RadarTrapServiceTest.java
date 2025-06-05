package test.service;

import model.Car;
import model.Driver;
import model.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.RadarTrapService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RadarTrapServiceTest {

    private RadarTrapService radarTrapService;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        Driver driver = new Driver("F001", "Anna", "Muster", model.LicenseClass.B);
        Car car = new Car("C001", "BMW", "320i", "S-XX-1234");

        Trip trip = new Trip("F001", "C001", 10000, 10200,
                LocalDateTime.parse("2024-01-01T10:00:00"),
                LocalDateTime.parse("2024-01-01T11:00:00"));

        radarTrapService = new RadarTrapService(List.of(driver), List.of(car), List.of(trip));
        System.setOut(new PrintStream(output));
    }

    @Test
    public void testFindDriverAtTimeSuccess() {
        radarTrapService.findDriverAtTime("S-XX-1234;2024-01-01T10:30:00");
        assertTrue(output.toString().contains("Fahrer:"));
    }

    @Test
    public void testFindDriverAtTimeNoMatch() {
        radarTrapService.findDriverAtTime("S-XX-1234;2024-01-01T12:00:00");
        assertTrue(output.toString().contains("Kein Fahrer gefunden."));
    }

    @Test
    public void testInvalidFormatThrows() {
        assertThrows(IllegalArgumentException.class, () -> radarTrapService.findDriverAtTime("S-XX-1234 2024-01-01T10:00:00"));
    }
}