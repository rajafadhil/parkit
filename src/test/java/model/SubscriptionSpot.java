package model;

import model.ParkingSpot;
import model.Vehicle;
import java.time.LocalDateTime;

public class SubscriptionSpot extends ParkingSpot {
    public SubscriptionSpot(String spotId) {
        super(spotId);
    }

    @Override
    public double calculateFee(Vehicle vehicle, LocalDateTime exitTime) {
        // Gratis jika validasi di occupy() lolos
        return 0.0;
    }

    // ✅ Override occupy agar hanya menerima kendaraan dengan langganan aktif
    @Override
    public void occupy(Vehicle vehicle) {
        // Di sini kita tidak bisa mengakses ParkingManager untuk cek langganan.
        // Maka, validasi dilakukan di ParkingManager sebelum occupy().
        // Tapi, jika tetap ingin di sini, kita bisa abaikan karena sudah divalidasi di findAvailableSpot
        super.occupy(vehicle);
    }
}