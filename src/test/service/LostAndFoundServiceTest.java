package test.service;

import model.Car;
import model.Driver;
import model.LicenseClass;
import model.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LostAndFoundService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für die "Lost & Found"-Funktion (Fahrer am selben Tag im selben Fahrzeug).
 */
public class LostAndFoundServiceTest {

    private LostAndFoundService lostAndFoundService;

    @BeforeEach
    public void setUp() {
        Driver d1 = new Driver("F001", "Anna", "Muster", LicenseClass.B);
        Driver d2 = new Driver("F002", "Max", "Beispiel", LicenseClass.B);
        Car car = new Car("C001", "VW", "Golf", "S-AB-1234");

        Trip t1 = new Trip("F001", "C001", 10000, 10100,
                LocalDateTime.parse("2024-08-13T08:00:00"), LocalDateTime.parse("2024-08-13T09:00:00"));
        Trip t2 = new Trip("F002", "C001", 10100, 10200,
                LocalDateTime.parse("2024-08-13T09:30:00"), LocalDateTime.parse("2024-08-13T10:00:00"));

        lostAndFoundService = new LostAndFoundService(List.of(d1, d2), List.of(car), List.of(t1, t2));
    }

    @Test
    public void testFindOtherDriversOnSameDay() {
        String result = lostAndFoundService.findOtherDrivers("F001;2024-08-13");
        assertTrue(result.contains("Max Beispiel"));
    }

    @Test
    public void testInvalidFormatThrows() {
        assertThrows(RuntimeException.class, () -> lostAndFoundService.findOtherDrivers("F001-2024-08-13"));
    }

    @Test
    public void testNoMatchFound() {
        String result = lostAndFoundService.findOtherDrivers("F001;2024-08-15");
        assertTrue(result.contains("Keine Fahrten für Fahrer"));
    }
}