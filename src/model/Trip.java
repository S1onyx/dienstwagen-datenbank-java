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

    // Checks if trip overlaps with a given date (even partially)
    public boolean overlapsWithDate(LocalDate date) {
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();
        return !endTime.isBefore(dayStart) && !startTime.isAfter(dayEnd);
    }

    @Override
    public String toString() {
        return "Fahrer %s mit Fahrzeug %s von %dkm bis %dkm (%dkm), Start: %s, Ende: %s (%s)"
                .formatted(driverId, carId, startKm, endKm, getDistance(), startTime, endTime, getDuration());
    }
}