package test.service;

import model.Car;
import model.Driver;
import model.Trip;
import org.junit.jupiter.api.Test;
import service.ImportService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImportServiceTest {

    @Test
    void testSuccessfulImport() {
        ImportService service = new ImportService();
        service.loadData("src/test/resources/test-import-valid.db");

        List<Driver> drivers = service.getDrivers();
        List<Car> cars = service.getCars();
        List<Trip> trips = service.getTrips();

        assertEquals(2, drivers.size());
        assertEquals(2, cars.size());
        assertEquals(2, trips.size());
    }

    @Test
    void testDuplicateDriverSkipped() {
        ImportService service = new ImportService();
        service.loadData("src/test/resources/test-import-invalid.db");

        List<Driver> drivers = service.getDrivers();

        // Einer der beiden doppelten Fahrer wird übersprungen → es bleibt nur einer
        assertEquals(1, drivers.size());
    }

    @Test
    void testInvalidDateSkipped() {
        ImportService service = new ImportService();
        service.loadData("src/test/resources/test-import-invalid.db");

        List<Trip> trips = service.getTrips();

        // Nur 1 gültige Fahrt sollte importiert worden sein
        assertEquals(1, trips.size());
    }

    @Test
    void testUnknownDriverInTripSkipped() {
        ImportService service = new ImportService();
        service.loadData("src/test/resources/test-import-invalid.db");

        List<Trip> trips = service.getTrips();

        // Die Fahrt mit unbekanntem Fahrer wird übersprungen
        assertTrue(trips.stream().noneMatch(t -> t.driverId().equals("UNKNOWN")));
    }

    @Test
    void testEndBeforeStartSkipped() {
        ImportService service = new ImportService();
        service.loadData("src/test/resources/test-import-invalid.db");

        List<Trip> trips = service.getTrips();

        // Die Fahrt mit Endzeit vor Startzeit wird übersprungen
        assertTrue(trips.stream().noneMatch(t -> t.startTime().isAfter(t.endTime())));
    }
}