package model;

import java.time.LocalDateTime;

public class ParkingSession {
    private final String spotId;
    private final String licensePlate;
    private final LocalDateTime entryTime;
    private final String vehicleType;
    private LocalDateTime exitTime;
    private double fee;
    private boolean eligibleForFreeWash; // ✅ FITUR CUCI GRATIS

    public ParkingSession(String spotId, String licensePlate, LocalDateTime entryTime, String vehicleType) {
        this.spotId = spotId;
        this.licensePlate = licensePlate;
        this.entryTime = entryTime;
        this.vehicleType = vehicleType;
    }

    // Getter & Setter
    public String getSpotId() { return spotId; }
    public String getLicensePlate() { return licensePlate; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public double getFee() { return fee; }
    public String getVehicleType() { return vehicleType; }
    public boolean isEligibleForFreeWash() { return eligibleForFreeWash; }

    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    public void setFee(double fee) { this.fee = fee; }
    public void setEligibleForFreeWash(boolean eligible) { this.eligibleForFreeWash = eligible; }
}