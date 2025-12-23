package dao;

import util.DBConnection;
import java.sql.*;

public class ParkingSpotDAO {

    // ===== SET SPOT TERISI =====
    public void occupySpot(String spotId) {
        String sql = """
            UPDATE parking_spots
            SET is_occupied = TRUE
            WHERE spot_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, spotId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ===== SET SPOT KOSONG =====
    public void releaseSpot(String spotId) {
        String sql = """
            UPDATE parking_spots
            SET is_occupied = FALSE
            WHERE spot_id = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, spotId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
