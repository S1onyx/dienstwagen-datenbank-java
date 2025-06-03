package model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record Trip(
        String driverId,
        String carId,
        int startKm,
        int endKm,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    // Calculates total driven distance
    public long getDistance() {
        return endKm - startKm;
    }

    // Calculates trip duration
    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    // Checks if timestamp is within trip time window
    public boolean includesTime(LocalDateTime timestamp) {
        return !timestamp.isBefore(startTime) && !timestamp.isAfter(endTime);
    }

    // Checks if trip starts on given date
    public boolean isOnDate(LocalDate date) {
        return startTime.toLocalDate().equals(date);
    }

    @Override
    public String toString() {
        return "Fahrer %s mit Fahrzeug %s von %dkm bis %dkm, Start: %s, Ende: %s, Strecke: %dkm"
                .formatted(driverId, carId, startKm, endKm, startTime, endTime, getDistance());
    }
}