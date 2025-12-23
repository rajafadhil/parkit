package model;

import model.ParkingSession;
import model.Vehicle;
import java.time.LocalDateTime;

public abstract class ParkingSpot {
    protected String spotId;
    protected boolean isOccupied = false;
    protected LocalDateTime entryTime;
    protected Vehicle currentVehicle;

    public ParkingSpot(String spotId) {
        this.spotId = spotId;
    }

    public abstract double calculateFee(Vehicle vehicle, LocalDateTime exitTime);

    public void occupy(Vehicle vehicle) {
        if (vehicle == null) return;
        this.currentVehicle = vehicle;
        this.entryTime = LocalDateTime.now();
        this.isOccupied = true;
    }

    public ParkingSession release(LocalDateTime exitTime) {
        if (!isOccupied || exitTime == null || currentVehicle == null) {
            return null;
        }
        double fee = calculateFee(currentVehicle, exitTime);
        ParkingSession session = new ParkingSession(spotId, currentVehicle.getLicensePlate(), entryTime, currentVehicle.getType());
        session.setExitTime(exitTime);
        session.setFee(fee);
        // Reset
        this.isOccupied = false;
        this.currentVehicle = null;
        this.entryTime = null;
        return session;
    }

    public boolean isAvailable() {
        return !isOccupied;
    }

    // Getter
    public String getSpotId() { return spotId; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public Vehicle getCurrentVehicle() { return currentVehicle; }
    public boolean isOccupied() { return isOccupied; }
}