package utils;

import exceptions.EntityNotFoundException;
import model.Car;
import model.Driver;

import java.util.List;

/**
 * Hilfsklasse für das sichere Suchen von Fahrern und Fahrzeugen in Listen.
 * <p>
 * Wird genutzt in Services wie LostAndFound und RadarTrap.
 */
public class EntityFinderUtils {

    /**
     * Sucht nach einem Fahrer anhand der Fahrer-ID.
     *
     * @param drivers Liste aller verfügbaren Fahrer
     * @param id      ID des gesuchten Fahrers
     * @return gefundener {@link Driver}
     * @throws EntityNotFoundException wenn kein passender Fahrer gefunden wurde
     */
    public static Driver findDriverById(List<Driver> drivers, String id) {
        return drivers.stream()
                .filter(d -> d.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Fahrer mit ID \"" + id + "\" nicht gefunden."));
    }

    /**
     * Sucht nach einem Fahrzeug anhand der Fahrzeug-ID.
     *
     * @param cars Liste aller verfügbaren Fahrzeuge
     * @param id   ID des gesuchten Fahrzeugs
     * @return gefundenes {@link Car}
     * @throws EntityNotFoundException wenn kein Fahrzeug mit dieser ID existiert
     */
    public static Car findCarById(List<Car> cars, String id) {
        return cars.stream()
                .filter(c -> c.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Fahrzeug mit ID \"" + id + "\" nicht gefunden."));
    }

    /**
     * Sucht nach einem Fahrzeug anhand des Kennzeichens (nicht case-sensitiv).
     *
     * @param cars          Liste aller Fahrzeuge
     * @param licensePlate  Kennzeichen (z. B. S-AB-1234)
     * @return gefundenes {@link Car}
     * @throws EntityNotFoundException wenn kein passendes Fahrzeug gefunden wurde
     */
    public static Car findCarByLicensePlate(List<Car> cars, String licensePlate) {
        return cars.stream()
                .filter(c -> c.licensePlate().equalsIgnoreCase(licensePlate))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Kein Fahrzeug mit dem Kennzeichen \"" + licensePlate + "\" gefunden."));
    }
}