package model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Repräsentiert eine Fahrt, bestehend aus Fahrer-ID, Fahrzeug-ID, Start-/End-Kilometerständen und Zeitangaben.
 *
 * Diese Klasse verwendet das Record-Konstrukt für eine unveränderliche Datenstruktur.
 *
 * @param driverId   ID des Fahrers
 * @param carId      ID des Fahrzeugs
 * @param startKm    Kilometerstand bei Fahrtbeginn
 * @param endKm      Kilometerstand bei Fahrtende
 * @param startTime  Startzeitpunkt
 * @param endTime    Endzeitpunkt
 */
public record Trip(
        String driverId,
        String carId,
        int startKm,
        int endKm,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    /**
     * Berechnet die insgesamt gefahrene Strecke in Kilometern.
     *
     * @return Differenz aus End- und Start-Kilometer
     */
    public long getDistance() {
        return endKm - startKm;
    }

    /**
     * Berechnet die Dauer der Fahrt.
     *
     * @return Differenz zwischen Start- und Endzeit als {@link Duration}
     */
    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    /**
     * Prüft, ob ein gegebener Zeitpunkt innerhalb der Fahrzeit liegt.
     *
     * @param timestamp Zeitpunkt zur Prüfung
     * @return true, falls der Zeitpunkt zwischen Start- und Endzeit liegt
     */
    public boolean includesTime(LocalDateTime timestamp) {
        return !timestamp.isBefore(startTime) && !timestamp.isAfter(endTime);
    }

    /**
     * Prüft, ob diese Fahrt (auch teilweise) mit einem bestimmten Datum überlappt.
     *
     * @param date Tag, mit dem verglichen wird
     * @return true, wenn Start- oder Endzeit im betrachteten Kalendertag liegt
     */
    public boolean overlapsWithDate(LocalDate date) {
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();
        return !endTime.isBefore(dayStart) && !startTime.isAfter(dayEnd);
    }

    /**
     * Gibt eine formatierte Beschreibung der Fahrt zurück.
     *
     * @return Beschreibung mit Fahrer, Fahrzeug, Kilometern und Zeiten
     */
    @Override
    public String toString() {
        return "Fahrer %s mit Fahrzeug %s von %dkm bis %dkm (%dkm), Start: %s, Ende: %s (%s)"
                .formatted(driverId, carId, startKm, endKm, getDistance(), startTime, endTime, getDuration());
    }
}