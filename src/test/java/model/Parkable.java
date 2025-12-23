package model;

public interface Parkable {
    boolean park(Vehicle vehicle, String spotType);
    ParkingSession unpark(String licensePlate);
}