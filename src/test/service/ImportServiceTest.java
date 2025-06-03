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

        // Check that all expected entries were imported
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

        // Only one of the duplicate drivers should remain
        List<Driver> drivers = service.getDrivers();
        assertEquals(1, drivers.size());
    }

    @Test
    void testInvalidDateSkipped() {
        ImportService service = new ImportService();
        service.loadData("src/test/resources/test-import-invalid.db");

        // One trip is valid, the other has invalid date
        List<Trip> trips = service.getTrips();
        assertEquals(1, trips.size());
    }

    @Test
    void testUnknownDriverInTripSkipped() {
        ImportService service = new ImportService();
        service.loadData("src/test/resources/test-import-invalid.db");

        // Trip with unknown driver should be ignored
        List<Trip> trips = service.getTrips();
        assertTrue(trips.stream().noneMatch(t -> t.driverId().equals("UNKNOWN")));
    }

    @Test
    void testEndBeforeStartSkipped() {
        ImportService service = new ImportService();
        service.loadData("src/test/resources/test-import-invalid.db");

        // Trips with invalid time range should not be imported
        List<Trip> trips = service.getTrips();
        assertTrue(trips.stream().noneMatch(t -> t.startTime().isAfter(t.endTime())));
    }
}