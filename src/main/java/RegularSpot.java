import java.time.LocalDateTime;
import java.time.Duration;

public class RegularSpot extends ParkingSpot {
    public RegularSpot(String spotId) {
        super(spotId);
    }

    @Override
    public double calculateFee(Vehicle vehicle, LocalDateTime exitTime) {
        long minutes = Duration.between(entryTime, exitTime).toMinutes();
        if (minutes <= 0) minutes = 1;
        long hours = (minutes + 59) / 60;

        if ("MOTOR".equals(vehicle.getType())) {
            return hours * 3000;
        } else { // MOBIL
            return hours * 5000;
        }
    }
}