import java.time.LocalDateTime;
import java.time.Duration;

public class PremiumSpot extends ParkingSpot {
    public PremiumSpot(String spotId) {
        super(spotId);
    }

    @Override
    public double calculateFee(Vehicle vehicle, LocalDateTime exitTime) {
        // ✅ Validasi null untuk mencegah error
        if (entryTime == null || vehicle == null || exitTime == null) {
            return 0.0;
        }

        // Hitung durasi
        long minutes = Duration.between(entryTime, exitTime).toMinutes();
        if (minutes <= 0) minutes = 1;
        long hours = (minutes + 59) / 60; // pembulatan ke atas

        // Ambil jenis kendaraan
        String type = vehicle.getType();

        // Hitung tarif reguler berdasarkan jenis kendaraan
        double baseFee;
        if ("MOTOR".equals(type)) {
            baseFee = hours * 3000;
        } else if ("MOBIL".equals(type)) {
            baseFee = hours * 5000;
        } else {
            // Default jika jenis tidak dikenal (misalnya, jika truk masih bisa masuk)
            baseFee = hours * 5000;
        }

        // Kembalikan tarif premium (2x tarif reguler)
        return baseFee * 2;
    }
}