package model;

public class Vehicle {
    private int id;
    private final String licensePlate;
    private final String type; // "MOTOR" atau "MOBIL"

    // Constructor kosong
    public Vehicle() {
        this.id = 0;
        this.licensePlate = "";
        this.type = "MOTOR";
    }

    // Constructor utama (tanpa ownerName)
    public Vehicle(String licensePlate, String type) {
        if (licensePlate == null || type == null) {
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
    }

    // Constructor untuk DAO (dengan ID)
    public Vehicle(int id, String licensePlate, String type) {
        this.id = id;
        if (licensePlate == null || type == null) {
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
    }

    // Getter
    public int getId() { return id; }
    public String getLicensePlate() { return licensePlate; }
    public String getType() { return type; }

    // Setter (hanya untuk ID)
    public void setId(int id) { this.id = id; }
}