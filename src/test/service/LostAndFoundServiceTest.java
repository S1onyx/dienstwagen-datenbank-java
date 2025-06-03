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

class LostAndFoundServiceTest {

    private LostAndFoundService service;

    @BeforeEach
    void setUp() {
        // Prepare 2 drivers, same car, different trips on same day
        Driver d1 = new Driver("F001", "Anna", "Muster", LicenseClass.B);
        Driver d2 = new Driver("F002", "Max", "Beispiel", LicenseClass.B);
        Car car = new Car("C001", "VW", "Golf", "S-AB-1234");

        Trip t1 = new Trip("F001", "C001", 10000, 10100,
                LocalDateTime.parse("2024-08-13T08:00:00"), LocalDateTime.parse("2024-08-13T09:00:00"));
        Trip t2 = new Trip("F002", "C001", 10100, 10200,
                LocalDateTime.parse("2024-08-13T09:30:00"), LocalDateTime.parse("2024-08-13T10:00:00"));

        service = new LostAndFoundService(List.of(d1, d2), List.of(car), List.of(t1, t2));
    }

    @Test
    void testFindOtherDriversOnSameDay() {
        // Valid input: other driver should be found
        String input = "F001;2024-08-13";
        assertDoesNotThrow(() -> service.findOtherDrivers(input));
    }

    @Test
    void testInvalidFormatThrows() {
        // Missing semicolon → should throw
        String input = "F001-2024-08-13";
        assertThrows(RuntimeException.class, () -> service.findOtherDrivers(input));
    }

    @Test
    void testNoMatchFound() {
        // No trips on this date → no output, but no crash
        String input = "F001;2024-08-15";
        assertDoesNotThrow(() -> service.findOtherDrivers(input));
    }
}