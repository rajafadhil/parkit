package dao;

import util.DBConnection;
import java.sql.*;
import java.time.LocalDateTime;

public class ParkingSpotDAO {

    /**
     * Menyimpan kendaraan ke dalam spot parkir di database.
     * Hanya berhasil jika spot tersedia (is_occupied = 0).
     *
     * @param spotId        ID spot (misal: "R1", "P1")
     * @param licensePlate  Plat nomor kendaraan
     * @param spotType      Tipe spot ("REGULER" atau "PREMIUM") — sesuai ENUM di database
     * @return true jika berhasil
     */
    public boolean occupySpot(String spotId, String licensePlate, String spotType) {
        if (spotId == null || licensePlate == null || spotType == null ||
            spotId.isEmpty() || licensePlate.isEmpty() || spotType.isEmpty()) {
            System.err.println("⚠️ [DAO] occupySpot: Parameter tidak boleh null/kosong");
            return false;
        }

        String sql = """
            UPDATE parking_spots
            SET is_occupied = 1,
                vehicle_plate = ?,
                entry_time = ?,
                spot_type = ?
            WHERE spot_id = ? AND is_occupied = 0
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, licensePlate);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(3, spotType); // ✅ Update spot_type juga
            ps.setString(4, spotId);

            int rowsAffected = ps.executeUpdate();
            System.out.println("🔍 [DAO] occupySpot: " + rowsAffected + " baris di-update untuk spot " + spotId);

            if (rowsAffected == 0) {
                System.err.println("⚠️ [DAO] Tidak ada baris di-update untuk spot " + spotId +
                    ". Spot mungkin sudah terisi atau tidak ada.");
            }

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("💥 [DAO] ERROR di occupySpot untuk spot " + spotId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengambil data kendaraan yang sedang parkir di suatu spot.
     */
    public SpotData getSpotData(String spotId) {
        if (spotId == null || spotId.isEmpty()) {
            return null;
        }

        String sql = "SELECT vehicle_plate, entry_time, spot_type FROM parking_spots WHERE spot_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, spotId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String plate = rs.getString("vehicle_plate");
                    Timestamp entry = rs.getTimestamp("entry_time");
                    String spotType = rs.getString("spot_type");
                    LocalDateTime entryTime = (entry != null) ? entry.toLocalDateTime() : null;
                    return new SpotData(plate, entryTime, spotType);
                }
            }
        } catch (SQLException e) {
            System.err.println("💥 [DAO] ERROR di getSpotData untuk spot " + spotId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Melepas spot: mengosongkan data kendaraan dan waktu.
     */
    public boolean releaseSpot(String spotId) {
        if (spotId == null || spotId.isEmpty()) {
            return false;
        }

        String sql = """
            UPDATE parking_spots
            SET is_occupied = 0,
                vehicle_plate = NULL,
                entry_time = NULL
            WHERE spot_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, spotId);
            int rowsAffected = ps.executeUpdate();
            System.out.println("🔍 [DAO] releaseSpot: " + rowsAffected + " baris di-reset untuk spot " + spotId);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("💥 [DAO] ERROR di releaseSpot untuk spot " + spotId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Kelas bantu
    public static class SpotData {
        private final String licensePlate;
        private final LocalDateTime entryTime;
        private final String spotType;

        public SpotData(String licensePlate, LocalDateTime entryTime, String spotType) {
            this.licensePlate = licensePlate;
            this.entryTime = entryTime;
            this.spotType = spotType;
        }

        public String getLicensePlate() { return licensePlate; }
        public LocalDateTime getEntryTime() { return entryTime; }
        public String getSpotType() { return spotType; }
    }
}