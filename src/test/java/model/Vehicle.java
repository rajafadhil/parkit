package model;

public class Vehicle {
    private int id;
    private final String licensePlate;
    private final String type; // "MOTOR" atau "MOBIL"

    /**
     * Constructor untuk membuat kendaraan baru (tanpa ID).
     * Digunakan saat registrasi dari form.
     *
     * @param licensePlate plat nomor (tidak boleh null/kosong)
     * @param type         "MOTOR" atau "MOBIL"
     * @throws IllegalArgumentException jika data tidak valid
     */
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

    /**
     * Constructor untuk membangun objek dari database (dengan ID).
     *
     * @param id           ID dari database
     * @param licensePlate plat nomor
     * @param type         "MOTOR" atau "MOBIL"
     */
    public Vehicle(int id, String licensePlate, String type) {
        this(licensePlate, type); // delegasi ke constructor utama
        this.id = id;
    }

    // Getter
    public int getId() { return id; }
    public String getLicensePlate() { return licensePlate; }
    public String getType() { return type; }

    // Setter hanya untuk ID (digunakan setelah insert ke DB)
    public void setId(int id) { this.id = id; }

    // Optional: override toString() untuk debugging
    @Override
    public String toString() {
        return "Vehicle{id=" + id + ", licensePlate='" + licensePlate + "', type='" + type + "'}";
    }
}