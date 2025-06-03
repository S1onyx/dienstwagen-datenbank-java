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

class RadarTrapServiceTest {

    private RadarTrapService radarService;

    @BeforeEach
    void setUp() {
        Driver driver = new Driver("F001", "Anna", "Muster", model.LicenseClass.B);
        Car car = new Car("C001", "BMW", "320i", "S-XX-1234");

        Trip trip = new Trip(
                "F001",
                "C001",
                10000,
                10200,
                LocalDateTime.parse("2024-01-01T10:00:00"),
                LocalDateTime.parse("2024-01-01T11:00:00")
        );

        radarService = new RadarTrapService(List.of(driver), List.of(car), List.of(trip));
    }

    @Test
    void testFindDriverAtTime() {
        String input = "S-XX-1234;2024-01-01T10:30:00";

        // Es soll kein Fehler auftreten
        assertDoesNotThrow(() -> radarService.findDriverAtTime(input));
    }

    @Test
    void testFindDriverAtTimeNoMatch() {
        String input = "S-XX-1234;2024-01-01T12:00:00";

        // Kein Treffer, aber auch kein Fehler – nur "Kein Fahrer gefunden."
        assertDoesNotThrow(() -> radarService.findDriverAtTime(input));
    }

    @Test
    void testInvalidFormatThrows() {
        String input = "S-XX-1234 2024-01-01T10:00:00"; // fehlt das Semikolon

        assertThrows(IllegalArgumentException.class, () -> radarService.findDriverAtTime(input));
    }
}