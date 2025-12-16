import java.time.LocalDate;

public class Subscription {
    private final String licensePlate;
    private final LocalDate startDate;
    private final LocalDate expiryDate;

    public Subscription(String licensePlate, LocalDate startDate, LocalDate expiryDate) {
        if (licensePlate == null || startDate == null || expiryDate == null) {
            throw new IllegalArgumentException("Plat, tanggal mulai, dan tanggal berakhir tidak boleh null.");
        }
        if (startDate.isAfter(expiryDate)) {
            throw new IllegalArgumentException("Tanggal mulai tidak boleh setelah tanggal berakhir.");
        }
        this.licensePlate = licensePlate.toUpperCase().trim();
        if (this.licensePlate.isEmpty()) {
            throw new IllegalArgumentException("Plat nomor tidak boleh kosong.");
        }
        this.startDate = startDate;
        this.expiryDate = expiryDate;
    }

    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return !now.isBefore(startDate) && !now.isAfter(expiryDate);
    }

    public String getLicensePlate() { return licensePlate; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
}