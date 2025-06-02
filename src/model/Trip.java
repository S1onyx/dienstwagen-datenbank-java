package model;

import java.time.LocalDateTime;

public class Trip {
    private final String driverId;
    private final String carId;
    private final int startKm;
    private final int endKm;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public Trip(String driverId, String carId, int startKm, int endKm, LocalDateTime startTime, LocalDateTime endTime) {
        this.driverId = driverId;
        this.carId = carId;
        this.startKm = startKm;
        this.endKm = endKm;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getCarId() {
        return carId;
    }

    public int getStartKm() {
        return startKm;
    }

    public int getEndKm() {
        return endKm;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "driverId='" + driverId + '\'' +
                ", carId='" + carId + '\'' +
                ", startKm=" + startKm +
                ", endKm=" + endKm +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}