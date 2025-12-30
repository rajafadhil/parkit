package dao;

import java.sql.*;
import java.time.LocalDate;
import util.DBConnection;

public class SubscriptionDAO {

    /**
     * Menambahkan langganan baru untuk kendaraan.
     *
     * @param vehicleId ID kendaraan (harus > 0)
     * @param start     Tanggal mulai (tidak boleh null)
     * @param expiry    Tanggal berakhir (tidak boleh null)
     * @return true jika berhasil disimpan
     */
    public boolean addSubscription(int vehicleId, LocalDate start, LocalDate expiry) {
        if (vehicleId <= 0) {
            System.err.println("❌ [SubscriptionDAO] vehicleId tidak valid: " + vehicleId);
            return false;
        }
        if (start == null || expiry == null) {
            System.err.println("❌ [SubscriptionDAO] Tanggal mulai/berakhir tidak boleh null");
            return false;
        }
        if (expiry.isBefore(start)) {
            System.err.println("❌ [SubscriptionDAO] Tanggal berakhir sebelum tanggal mulai");
            return false;
        }

        String sql = """
            INSERT INTO subscriptions (vehicle_id, start_date, expiry_date, is_active)
            VALUES (?, ?, ?, TRUE)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehicleId);
            ps.setDate(2, Date.valueOf(start));
            ps.setDate(3, Date.valueOf(expiry));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ [SubscriptionDAO] Langganan berhasil disimpan untuk vehicle_id=" + vehicleId);
                return true;
            } else {
                System.err.println("⚠️ [SubscriptionDAO] Tidak ada baris yang diinsert (mungkin constraint gagal)");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("💥 [SubscriptionDAO] ERROR SQL saat insert langganan:");
            System.err.println("   Message: " + e.getMessage());
            System.err.println("   SQLState: " + e.getSQLState());
            System.err.println("   ErrorCode: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cek apakah kendaraan memiliki langganan aktif yang belum expired.
     */
    public boolean hasActiveSubscription(int vehicleId) {
        if (vehicleId <= 0) return false;

        String sql = """
            SELECT 1 FROM subscriptions
            WHERE vehicle_id = ?
              AND is_active = TRUE
              AND expiry_date >= CURDATE()
            LIMIT 1
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehicleId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("❌ [SubscriptionDAO] Gagal cek langganan aktif: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Nonaktifkan langganan yang sudah expired.
     */
    public int deactivateExpiredSubscriptions() {
        String sql = """
            UPDATE subscriptions
            SET is_active = FALSE
            WHERE is_active = TRUE AND expiry_date < CURDATE()
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("🔄 [SubscriptionDAO] " + rows + " langganan expired dinonaktifkan.");
            }
            return rows;

        } catch (SQLException e) {
            System.err.println("❌ [SubscriptionDAO] Gagal nonaktifkan langganan expired: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // === KELAS BANTU ===
    public static class SubscriptionData {
        private final int id;
        private final LocalDate startDate;
        private final LocalDate expiryDate;

        public SubscriptionData(int id, LocalDate startDate, LocalDate expiryDate) {
            this.id = id;
            this.startDate = startDate;
            this.expiryDate = expiryDate;
        }

        public int getId() { return id; }
        public LocalDate getStartDate() { return startDate; }
        public LocalDate getExpiryDate() { return expiryDate; }
    }
}