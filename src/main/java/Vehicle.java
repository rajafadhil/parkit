public class Vehicle {
    private final String licensePlate;
    private final String type; // "MOTOR" atau "MOBIL"
    private final String ownerName;

    public Vehicle(String licensePlate, String type, String ownerName) {
        if (licensePlate == null || type == null || ownerName == null) {
            throw new IllegalArgumentException("Data tidak boleh null.");
        }
        this.licensePlate = licensePlate.trim().toUpperCase();
        if (this.licensePlate.isEmpty()) {
            throw new IllegalArgumentException("Plat tidak boleh kosong.");
        }
        String upperType = type.trim().toUpperCase();
        if (!"MOTOR".equals(upperType) && !"MOBIL".equals(upperType)) {
            throw new IllegalArgumentException("Jenis kendaraan harus MOTOR atau MOBIL.");
        }
        this.type = upperType;
        this.ownerName = ownerName.trim();
    }

    public String getLicensePlate() { return licensePlate; }
    public String getType() { return type; }
    public String getOwnerName() { return ownerName; }
}