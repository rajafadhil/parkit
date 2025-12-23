package model;

import java.time.LocalDate;

public class Subscription {

    private String licensePlate;
    private LocalDate startDate;
    private LocalDate expiryDate;

    public Subscription(String licensePlate, LocalDate startDate, LocalDate expiryDate) {
        this.licensePlate = licensePlate;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public boolean isActive() {
        return expiryDate != null && !expiryDate.isBefore(LocalDate.now());
    }
}
