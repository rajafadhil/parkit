package dao;

import java.sql.*;
import java.time.LocalDate;
import model.Subscription;
import util.DBConnection;

public class SubscriptionDAO {

    // ================= TAMBAH LANGGANAN =================
    public boolean addSubscription(int vehicleId, LocalDate start, LocalDate expiry) {
        String sql = """
            INSERT INTO subscriptions (vehicle_id, start_date, expiry_date, is_active)
            VALUES (?, ?, ?, TRUE)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehicleId);
            ps.setDate(2, Date.valueOf(start));
            ps.setDate(3, Date.valueOf(expiry));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================= CEK LANGGANAN AKTIF =================
    public boolean hasActiveSubscription(int vehicleId) {
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
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================= NONAKTIFKAN LANGGANAN =================
    public void deactivateExpiredSubscriptions() {
        String sql = """
            UPDATE subscriptions
            SET is_active = FALSE
            WHERE expiry_date < CURDATE()
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
