package test.service;

import model.Driver;
import service.CommandHandler;
import service.LostAndFoundService;
import service.RadarTrapService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandHandlerFahrersucheTest {

    @Test
    void testFahrersucheFindetTreffer() {
        Driver d1 = new Driver("F001", "Anna", "Musterfrau", model.LicenseClass.B);
        Driver d2 = new Driver("F002", "Max", "Beispiel", model.LicenseClass.B);
        List<Driver> drivers = List.of(d1, d2);

        CommandHandler handler = new CommandHandler(
                drivers,
                List.of(),
                new RadarTrapService(drivers, List.of(), List.of()),
                new LostAndFoundService(drivers, List.of(), List.of())
        );

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        handler.handle("--fahrersuche=anna");

        String result = output.toString().toLowerCase();
        assertTrue(result.contains("anna musterfrau"));
    }
}