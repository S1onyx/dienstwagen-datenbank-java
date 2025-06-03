package test.service;

import model.Car;
import service.CommandHandler;
import service.LostAndFoundService;
import service.RadarTrapService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandHandlerFahrzeugsucheTest {

    @Test
    void testFahrzeugsucheFindetTreffer() {
        Car c1 = new Car("C001", "BMW", "320i", "S-XX-1234");
        Car c2 = new Car("C002", "Audi", "A4", "S-AA-9876");
        List<Car> cars = List.of(c1, c2);

        CommandHandler handler = new CommandHandler(
                List.of(),
                cars,
                new RadarTrapService(List.of(), cars, List.of()),
                new LostAndFoundService(List.of(), cars, List.of())
        );

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        handler.handle("--fahrzeugsuche=bmw");

        String result = output.toString().toLowerCase();
        assertTrue(result.contains("bmw 320i"));
    }
}