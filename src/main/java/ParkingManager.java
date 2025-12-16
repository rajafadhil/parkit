import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;

public class ParkingManager implements Parkable {
    private List<ParkingSpot> spots;
    private TransactionLog log;
    private Map<String, Vehicle> vehicles;
    private Map<String, Subscription> subscriptions;

    public ParkingManager() {
        this.spots = new ArrayList<>();
        this.log = new TransactionLog();
        this.vehicles = new HashMap<>();
        this.subscriptions = new HashMap<>();
        initializeSpots();
    }

    private void initializeSpots() {
        for (int i = 1; i <= 3; i++) spots.add(new RegularSpot("R" + i));
        for (int i = 1; i <= 2; i++) spots.add(new PremiumSpot("P" + i));
        spots.add(new SubscriptionSpot("S1"));
    }

    // --- Manajemen Kendaraan (Ardan Pratama Y.) ---
    public boolean registerVehicle(String licensePlate, String type, String ownerName) {
        licensePlate = licensePlate.toUpperCase().trim();
        if (licensePlate.isEmpty() || type == null || ownerName == null) return false;
        if (vehicles.containsKey(licensePlate)) return false;
        vehicles.put(licensePlate, new Vehicle(licensePlate, type, ownerName));
        return true;
    }

    public void registerSubscription(String licensePlate, LocalDate start, LocalDate expiry) {
        licensePlate = licensePlate.toUpperCase().trim();
        subscriptions.put(licensePlate, new Subscription(licensePlate, start, expiry));
    }

    public boolean isVehicleRegistered(String licensePlate) {
        return vehicles.containsKey(licensePlate.toUpperCase());
    }

    // --- Implementasi Parkable ---
    @Override
    public boolean park(Vehicle vehicle, String spotType) {
        if (vehicle == null || spotType == null) return false;
        String license = vehicle.getLicensePlate().toUpperCase().trim();
        if (license.isEmpty()) return false;

        // Daftarkan otomatis jika belum ada
        if (!vehicles.containsKey(license)) {
            vehicles.put(license, vehicle);
        }

        // Cegah parkir ganda
        if (isVehicleActive(license)) return false;

        // Cari spot yang sesuai
        ParkingSpot spot = findAvailableSpot(license, spotType);
        if (spot == null) return false;

        // Parkir
        spot.occupy(vehicle);
        ParkingSession session = new ParkingSession(
            spot.getSpotId(), license, spot.getEntryTime(), vehicle.getType()
        );

        // ✅ Cuci gratis jika Premium atau Langganan aktif
        if ("PREMIUM".equals(spotType) || ("LANGGANAN".equals(spotType) && isEligibleSubscription(license))) {
            session.setEligibleForFreeWash(true);
        }

        log.logSession(session);
        return true;
    }

    @Override
    public ParkingSession unpark(String licensePlate) {
        if (licensePlate == null) return null;
        licensePlate = licensePlate.toUpperCase().trim();

        for (ParkingSpot spot : spots) {
            if (spot.isOccupied() && 
                spot.getCurrentVehicle() != null && 
                licensePlate.equals(spot.getCurrentVehicle().getLicensePlate())) {

                LocalDateTime exitTime = LocalDateTime.now();
                double fee = 0.0;

                // Hitung tarif
                if (spot instanceof SubscriptionSpot) {
                    if (isEligibleSubscription(licensePlate)) {
                        fee = 0.0; // Gratis
                    } else {
                        // Tidak aktif → hitung sebagai reguler
                        RegularSpot temp = new RegularSpot("TEMP");
                        temp.occupy(spot.getCurrentVehicle());
                        temp.entryTime = spot.getEntryTime();
                        fee = temp.calculateFee(spot.getCurrentVehicle(), exitTime);
                    }
                } else {
                    fee = spot.calculateFee(spot.getCurrentVehicle(), exitTime);
                }

                // Lepaskan spot
                spot.release(exitTime);

                // Update session
                for (ParkingSession s : log.getActiveSessions()) {
                    if (licensePlate.equals(s.getLicensePlate())) {
                        s.setExitTime(exitTime);
                        s.setFee(fee);
                        // Cuci gratis saat keluar tetap sesuai status saat masuk
                        return s;
                    }
                }
            }
        }
        return null;
    }

    // --- Logika Internal ---
    private ParkingSpot findAvailableSpot(String licensePlate, String spotType) {
        licensePlate = licensePlate.toUpperCase();

        if ("LANGGANAN".equals(spotType)) {
            if (!isEligibleSubscription(licensePlate)) return null; // ✅ Cegah akses jika tidak aktif
            for (ParkingSpot spot : spots) {
                if (spot instanceof SubscriptionSpot && spot.isAvailable()) return spot;
            }
        } else if ("PREMIUM".equals(spotType)) {
            for (ParkingSpot spot : spots) {
                if (spot instanceof PremiumSpot && spot.isAvailable()) return spot;
            }
        } else if ("REGULER".equals(spotType)) {
            for (ParkingSpot spot : spots) {
                if (spot instanceof RegularSpot && spot.isAvailable()) return spot;
            }
        }
        return null;
    }

    public boolean isEligibleSubscription(String licensePlate) {
        Subscription sub = subscriptions.get(licensePlate);
        return sub != null && sub.isActive();
    }

    private boolean isVehicleActive(String licensePlate) {
        return log.getActiveSessions().stream()
            .anyMatch(s -> licensePlate.equals(s.getLicensePlate()));
    }

    // --- Getter untuk laporan ---
    public int getActiveVehicleCount() {
        return log.getActiveSessions().size();
    }

    public double getTodayRevenue() {
        return log.getTotalRevenue(LocalDate.now());
    }

    public TransactionLog getLog() {
        return log;
    }
}