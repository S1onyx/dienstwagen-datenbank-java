package test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ImportService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integrationstests für den ImportService.
 * Nutzt vorbereitete Testdateien unter /test/resources/.
 */
public class ImportServiceTest {

    private ImportService importService;

    @BeforeEach
    public void setUp() {
        importService = new ImportService();
    }

    @Test
    public void testSuccessfulImport() {
        importService.loadData("src/test/resources/test-import-valid.db");
        assertEquals(2, importService.getDrivers().size());
        assertEquals(2, importService.getCars().size());
        assertEquals(2, importService.getTrips().size());
    }

    @Test
    public void testDuplicateDriverSkipped() {
        importService.loadData("src/test/resources/test-import-invalid.db");
        assertEquals(1, importService.getDrivers().size());
    }

    @Test
    public void testInvalidDateSkipped() {
        importService.loadData("src/test/resources/test-import-invalid.db");
        assertEquals(1, importService.getTrips().size());
    }

    @Test
    public void testUnknownDriverInTripSkipped() {
        importService.loadData("src/test/resources/test-import-invalid.db");
        assertTrue(importService.getTrips().stream().noneMatch(t -> t.driverId().equals("UNKNOWN")));
    }

    @Test
    public void testEndBeforeStartSkipped() {
        importService.loadData("src/test/resources/test-import-invalid.db");
        assertTrue(importService.getTrips().stream().noneMatch(t -> t.startTime().isAfter(t.endTime())));
    }

    @Test
    public void testEmptyFileDoesNotCrash() {
        importService.loadData("src/test/resources/test-import-empty.db");
        assertTrue(importService.getDrivers().isEmpty());
        assertTrue(importService.getCars().isEmpty());
        assertTrue(importService.getTrips().isEmpty());
    }
}