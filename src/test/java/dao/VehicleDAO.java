package dao;

import model.Vehicle;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    // ================= CEK EXIST =================
    public boolean existsByPlate(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return false;
        }
        String sql = "SELECT 1 FROM vehicles WHERE license_plate = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, licensePlate);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("❌ [VehicleDAO] Error di existsByPlate: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ================= INSERT =================
    public boolean insertVehicle(Vehicle vehicle) {
        if (vehicle == null || vehicle.getLicensePlate() == null || vehicle.getType() == null) {
            return false;
        }
        String sql = "INSERT INTO vehicles (license_plate, vehicle_type) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, vehicle.getLicensePlate());
            ps.setString(2, vehicle.getType());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        vehicle.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("❌ [VehicleDAO] Error di insertVehicle: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ================= GET BY PLATE =================
    public Vehicle getVehicleByPlate(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return null;
        }
        String sql = "SELECT id, license_plate, vehicle_type FROM vehicles WHERE license_plate = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, licensePlate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Vehicle(
                        rs.getInt("id"),
                        rs.getString("license_plate"),
                        rs.getString("vehicle_type")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ [VehicleDAO] Error di getVehicleByPlate: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // ================= GET VEHICLE TYPE BY PLATE =================
    public String getVehicleTypeByPlate(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return null;
        }
        String sql = "SELECT vehicle_type FROM vehicles WHERE license_plate = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, licensePlate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("vehicle_type");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ [VehicleDAO] Error di getVehicleTypeByPlate: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // ================= GET VEHICLE ID BY PLATE =================
    public int getVehicleIdByPlate(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return -1;
        }
        String sql = "SELECT id FROM vehicles WHERE license_plate = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, licensePlate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ [VehicleDAO] Error di getVehicleIdByPlate: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    // ================= GET ALL =================
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT id, license_plate, vehicle_type FROM vehicles";
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
            System.err.println("❌ [VehicleDAO] Error di getAllVehicles: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // ================= DELETE =================
    public boolean deleteVehicle(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return false;
        }
        String sql = "DELETE FROM vehicles WHERE license_plate = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, licensePlate);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ [VehicleDAO] Error di deleteVehicle: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}