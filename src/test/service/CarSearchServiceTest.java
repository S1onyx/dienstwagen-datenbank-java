package test.service;

import model.Car;
import org.junit.jupiter.api.Test;
import service.CarSearchService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CarSearchServiceTest {

    @Test
    void testSearchByKeywordFindsMatch() {
        Car c1 = new Car("C001", "BMW", "320i", "S-XX-1234");
        Car c2 = new Car("C002", "Audi", "A4", "S-AA-9876");
        CarSearchService service = new CarSearchService(List.of(c1, c2));

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        service.searchByKeyword("bmw");

        String result = output.toString().toLowerCase();
        assertTrue(result.contains("bmw 320i"));
    }
}