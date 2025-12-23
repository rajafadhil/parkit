package util;

import model.SubscriptionSpot;
import model.Subscription;
import model.PremiumSpot;
import model.RegularSpot;
import model.ParkingSpot;
import model.ParkingSession;
import model.Vehicle;
import model.TransactionLog;
import model.Parkable;

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

    // --- Manajemen Kendaraan ---
    public boolean registerVehicle(String licensePlate, String type, String ownerName) {
        // Parameter ownerName dipertahankan untuk kompatibilitas,
        // tapi TIDAK DIGUNAKAN karena Vehicle tidak punya field ownerName
        if (licensePlate == null || type == null) {
            return false;
        }
        licensePlate = licensePlate.toUpperCase().trim();
        if (licensePlate.isEmpty()) {
            return false;
        }
        if (vehicles.containsKey(licensePlate)) {
            return false;
        }

        // ✅ Hanya gunakan licensePlate dan type
        vehicles.put(licensePlate, new Vehicle(licensePlate, type));
        return true;
    }

    public void registerSubscription(String licensePlate, LocalDate start, LocalDate expiry) {
        if (licensePlate == null) return;
        licensePlate = licensePlate.toUpperCase().trim();
        subscriptions.put(licensePlate, new Subscription(licensePlate, start, expiry));
    }

    public boolean isVehicleRegistered(String licensePlate) {
        if (licensePlate == null) return false;
        return vehicles.containsKey(licensePlate.toUpperCase());
    }

    // --- Implementasi Parkable ---
    @Override
    public boolean park(Vehicle vehicle, String spotType) {
        if (vehicle == null || spotType == null) return false;

        String license = vehicle.getLicensePlate();
        if (license == null) return false;
        license = license.toUpperCase().trim();
        if (license.isEmpty()) return false;

        // Jika belum terdaftar di manager, daftarkan
        if (!vehicles.containsKey(license)) {
            vehicles.put(license, vehicle);
        }

        if (isVehicleActive(license)) {
            return false; // Sudah parkir
        }

        ParkingSpot spot = findAvailableSpot(license, spotType);
        if (spot == null) {
            return false;
        }

        spot.occupy(vehicle);

        ParkingSession session = new ParkingSession(
                spot.getSpotId(),
                license,
                spot.getEntryTime(),
                vehicle.getType()
        );

        if ("PREMIUM".equals(spotType) ||
            ("LANGGANAN".equals(spotType) && isEligibleSubscription(license))) {
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
            if (spot.isOccupied()
                    && spot.getCurrentVehicle() != null
                    && licensePlate.equals(spot.getCurrentVehicle().getLicensePlate())) {

                LocalDateTime exitTime = LocalDateTime.now();
                double fee;

                if (spot instanceof SubscriptionSpot) {
                    if (isEligibleSubscription(licensePlate)) {
                        fee = 0.0;
                    } else {
                        // Hitung sebagai reguler
                        RegularSpot temp = new RegularSpot("TEMP");
                        temp.occupy(spot.getCurrentVehicle());
                        fee = temp.calculateFee(spot.getCurrentVehicle(), exitTime);
                    }
                } else {
                    fee = spot.calculateFee(spot.getCurrentVehicle(), exitTime);
                }

                spot.release(exitTime);

                // Update sesi di log
                for (ParkingSession s : log.getActiveSessions()) {
                    if (licensePlate.equals(s.getLicensePlate())) {
                        s.setExitTime(exitTime);
                        s.setFee(fee);
                        return s;
                    }
                }
            }
        }
        return null;
    }

    // --- Internal Logic ---
    private ParkingSpot findAvailableSpot(String licensePlate, String spotType) {
        if (licensePlate == null || spotType == null) return null;
        licensePlate = licensePlate.toUpperCase();

        if ("LANGGANAN".equals(spotType)) {
            if (!isEligibleSubscription(licensePlate)) return null;
            for (ParkingSpot spot : spots) {
                if (spot instanceof SubscriptionSpot && spot.isAvailable()) {
                    return spot;
                }
            }
        } else if ("PREMIUM".equals(spotType)) {
            for (ParkingSpot spot : spots) {
                if (spot instanceof PremiumSpot && spot.isAvailable()) {
                    return spot;
                }
            }
        } else if ("REGULER".equals(spotType)) {
            for (ParkingSpot spot : spots) {
                if (spot instanceof RegularSpot && spot.isAvailable()) {
                    return spot;
                }
            }
        }
        return null;
    }

    public boolean isEligibleSubscription(String licensePlate) {
        if (licensePlate == null) return false;
        Subscription sub = subscriptions.get(licensePlate.toUpperCase());
        return sub != null && sub.isActive();
    }

    private boolean isVehicleActive(String licensePlate) {
        if (licensePlate == null) return false;
        return log.getActiveSessions().stream()
                .anyMatch(s -> licensePlate.equals(s.getLicensePlate()));
    }

    // --- Getter ---
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