package dao;

import util.DBConnection;
import java.sql.*;
import java.time.LocalDateTime;

public class ParkingSessionDAO {

    /**
     * Menyimpan sesi parkir MASUK ke database (hanya waktu masuk).
     *
     * @param vehicleId ID kendaraan (dari tabel vehicles)
     * @param spotId    ID spot (misal: "P1", "R3")
     * @param entryTime Waktu masuk
     * @return true jika berhasil
     */
    public boolean insertSessionEntry(int vehicleId, String spotId, LocalDateTime entryTime) {
        String sql = """
            INSERT INTO parking_sessions (vehicle_id, spot_id, entry_time)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehicleId);
            ps.setString(2, spotId);
            ps.setTimestamp(3, Timestamp.valueOf(entryTime));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Gagal menyimpan sesi MASUK: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Memperbarui sesi parkir saat KELUAR (isi exit_time dan fee).
     *
     * @param vehicleId ID kendaraan
     * @param exitTime  Waktu keluar
     * @param fee       Biaya parkir
     * @return true jika berhasil
     */
    public boolean updateSessionExit(int vehicleId, LocalDateTime exitTime, double fee) {
        String sql = """
            UPDATE parking_sessions
            SET exit_time = ?, fee = ?
            WHERE vehicle_id = ?
              AND exit_time IS NULL
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(exitTime));
            ps.setDouble(2, fee);
            ps.setInt(3, vehicleId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                System.err.println("⚠️ Tidak ada sesi aktif ditemukan untuk kendaraan ID: " + vehicleId);
            }
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("❌ Gagal memperbarui sesi KELUAR: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * (Opsional) Menyimpan sesi lengkap sekaligus — hanya untuk testing.
     */
    public boolean saveCompleteSession(
            int vehicleId,
            String spotId,
            LocalDateTime entryTime,
            LocalDateTime exitTime,
            double fee) {

        String sql = """
            INSERT INTO parking_sessions (vehicle_id, spot_id, entry_time, exit_time, fee)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehicleId);
            ps.setString(2, spotId);
            ps.setTimestamp(3, Timestamp.valueOf(entryTime));
            ps.setTimestamp(4, Timestamp.valueOf(exitTime));
            ps.setDouble(5, fee);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Gagal menyimpan sesi lengkap: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghitung total pendapatan hari ini dari database.
     * Hanya menjumlahkan sesi yang sudah selesai (exit_time IS NOT NULL)
     * dan dimulai hari ini.
     *
     * @return total pendapatan hari ini, atau 0.0 jika tidak ada
     */
    public double getTodayRevenue() {
        String sql = """
            SELECT COALESCE(SUM(fee), 0)
            FROM parking_sessions
            WHERE DATE(entry_time) = CURDATE()
              AND exit_time IS NOT NULL
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("❌ Gagal mengambil pendapatan hari ini: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }
}