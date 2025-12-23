package dao;

import util.DBConnection;
import java.sql.*;
import java.time.LocalDateTime;

public class ParkingSessionDAO {

    // ===== SIMPAN SESI PARKIR MASUK =====
    public void insertSession(int vehicleId, String spotId, LocalDateTime entryTime) {
        String sql = """
            INSERT INTO parking_sessions (vehicle_id, spot_id, entry_time)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehicleId);
            ps.setString(2, spotId);
            ps.setTimestamp(3, Timestamp.valueOf(entryTime));

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Gagal menyimpan sesi parkir: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===== TUTUP SESI PARKIR KELUAR (AMAN DENGAN SPOT_ID) =====
    public void closeSession(String spotId, int vehicleId, LocalDateTime exitTime, double fee) {
        String sql = """
            UPDATE parking_sessions
            SET exit_time = ?, fee = ?
            WHERE vehicle_id = ?
              AND spot_id = ?
              AND exit_time IS NULL
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(exitTime));
            ps.setDouble(2, fee);
            ps.setInt(3, vehicleId);
            ps.setString(4, spotId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("⚠️ Tidak ada sesi aktif ditemukan untuk kendaraan ID=" + 
                    vehicleId + " di spot " + spotId);
            }

        } catch (SQLException e) {
            System.err.println("❌ Gagal menutup sesi parkir: " + e.getMessage());
            e.printStackTrace();
        }
    }
}