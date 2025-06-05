package test.service;

import model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.CarSearchService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CarSearchServiceTest {

    private CarSearchService carSearchService;

    @BeforeEach
    public void setUp() {
        Car c1 = new Car("C001", "BMW", "320i", "S-XX-1234");
        Car c2 = new Car("C002", "Audi", "A4", "S-AA-9876");
        Car c3 = new Car("C003", "Opel", "Corsa", "S-BB-1111");
        carSearchService = new CarSearchService(List.of(c1, c2, c3));
    }

    @Test
    public void testSearchByKeywordFindsMatch() {
        String result = carSearchService.searchByKeyword("bmw");
        assertTrue(result.toLowerCase().contains("bmw 320i"));
    }

    @Test
    public void testSearchByKeywordNoMatch() {
        String result = carSearchService.searchByKeyword("mercedes");
        assertTrue(result.toLowerCase().contains("keine passenden fahrzeuge gefunden"));
    }

    @Test
    public void testSearchByKeywordCaseInsensitive() {
        String result = carSearchService.searchByKeyword("aUdi");
        assertTrue(result.toLowerCase().contains("audi a4"));
    }

    @Test
    public void testSearchByPartialKeyword() {
        String result = carSearchService.searchByKeyword("cor");
        assertTrue(result.toLowerCase().contains("corsa"));
    }
}