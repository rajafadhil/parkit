import java.time.LocalDate;
import java.util.*;

public class TransactionLog {
    private final List<ParkingSession> sessions = new ArrayList<>();

    public void logSession(ParkingSession session) {
        sessions.add(session);
    }

    public void printHistory(String licensePlate) {
        licensePlate = licensePlate.toUpperCase();
        System.out.println("\n=== RIWAYAT PARKIR: " + licensePlate + " ===");
        boolean found = false;
        for (ParkingSession s : sessions) {
            if (licensePlate.equals(s.getLicensePlate())) {
                String exit = (s.getExitTime() != null) ? s.getExitTime().toString() : "MASIH PARKIR";
                String wash = s.isEligibleForFreeWash() ? "✅ Cuci Gratis" : "❌ Tidak";
                System.out.printf("Spot: %s | Masuk: %s | Keluar: %s | Tarif: Rp%.0f | %s%n",
                    s.getSpotId(), s.getEntryTime(), exit, s.getFee(), wash);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Tidak ada riwayat parkir.");
        }
    }

    public double getTotalRevenue(LocalDate date) {
        return sessions.stream()
            .filter(s -> s.getExitTime() != null)
            .filter(s -> LocalDate.from(s.getExitTime()).equals(date))
            .mapToDouble(ParkingSession::getFee)
            .sum();
    }

    public List<ParkingSession> getActiveSessions() {
        return sessions.stream()
            .filter(s -> s.getExitTime() == null)
            .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
    }
}