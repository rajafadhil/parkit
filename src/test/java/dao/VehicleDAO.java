package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Vehicle;
import util.DBConnection;

public class VehicleDAO {

    // ================= CEK EXIST =================
    public boolean existsByPlate(String licensePlate) {
        String sql = "SELECT 1 FROM vehicles WHERE license_plate = ? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, licensePlate);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================= INSERT =================
    public boolean insertVehicle(Vehicle vehicle) {
        System.out.println("🔍 [DAO] Memanggil insertVehicle untuk plat: " + vehicle.getLicensePlate());
        System.out.println("🔍 [DAO] Tipe: " + vehicle.getType());

        String sql = "INSERT INTO vehicles (license_plate, vehicle_type) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("🔗 [DAO] Koneksi DB berhasil didapat");

            ps.setString(1, vehicle.getLicensePlate());
            ps.setString(2, vehicle.getType());

            int rows = ps.executeUpdate();
            System.out.println("✅ [DAO] Berhasil insert " + rows + " baris");

            return rows > 0;

            } catch (SQLException e) {
            System.err.println("💥 [DAO] ERROR saat insert: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ================= GET BY PLATE =================
    public Vehicle getVehicleByPlate(String licensePlate) {
        String sql = "SELECT * FROM vehicles WHERE license_plate = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, licensePlate);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Vehicle(
                    rs.getInt("id"),
                    rs.getString("license_plate"),
                    rs.getString("vehicle_type")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ================= GET ALL =================
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicles";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Vehicle(
                    rs.getInt("id"),
                    rs.getString("license_plate"),
                    rs.getString("vehicle_type")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================= DELETE =================
    public boolean deleteVehicle(String licensePlate) {
        String sql = "DELETE FROM vehicles WHERE license_plate = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, licensePlate);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}