package test.service;

import model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CarSearchService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CarSearchServiceTest {

    private CarSearchService carSearchService;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        Car c1 = new Car("C001", "BMW", "320i", "S-XX-1234");
        Car c2 = new Car("C002", "Audi", "A4", "S-AA-9876");
        Car c3 = new Car("C003", "Opel", "Corsa", "S-BB-1111");
        carSearchService = new CarSearchService(List.of(c1, c2, c3));
        System.setOut(new PrintStream(output));
    }

    @Test
    public void testSearchByKeywordFindsMatch() {
        carSearchService.searchByKeyword("bmw");
        assertTrue(output.toString().toLowerCase().contains("bmw 320i"));
    }

    @Test
    public void testSearchByKeywordNoMatch() {
        carSearchService.searchByKeyword("mercedes");
        assertTrue(output.toString().toLowerCase().contains("keine passenden fahrzeuge gefunden"));
    }

    @Test
    public void testSearchByKeywordCaseInsensitive() {
        carSearchService.searchByKeyword("aUdi");
        assertTrue(output.toString().toLowerCase().contains("audi a4"));
    }

    @Test
    public void testSearchByPartialKeyword() {
        carSearchService.searchByKeyword("cor");
        assertTrue(output.toString().toLowerCase().contains("corsa"));
    }
}