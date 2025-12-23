package controller;

import model.Vehicle;
import util.ParkingManager;
import dao.VehicleDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/ParkInServlet")
public class ParkInServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Ambil atau inisialisasi ParkingManager di application scope
        ParkingManager manager =
                (ParkingManager) getServletContext().getAttribute("manager");
        if (manager == null) {
            manager = new ParkingManager();
            getServletContext().setAttribute("manager", manager);
        }

        VehicleDAO vehicleDAO = new VehicleDAO();

        // Ambil parameter
        String license = request.getParameter("licensePlate");
        String type = request.getParameter("vehicleType");
        String spotType = request.getParameter("spotType");

        // Validasi input
        if (license == null || type == null || spotType == null ||
            license.trim().isEmpty() || type.trim().isEmpty()) {
            session.setAttribute("msg", "❌ Semua field wajib diisi.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // Normalisasi input
        license = license.trim().toUpperCase();
        type = type.trim().toUpperCase();
        spotType = spotType.trim().toUpperCase();

        // Validasi jenis kendaraan
        if (!"MOTOR".equals(type) && !"MOBIL".equals(type)) {
            session.setAttribute("msg", "❌ Jenis kendaraan harus MOTOR atau MOBIL.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // Validasi jenis spot
        if (!"REGULER".equals(spotType)
                && !"PREMIUM".equals(spotType)
                && !"LANGGANAN".equals(spotType)) {
            session.setAttribute("msg",
                    "❌ Jenis spot harus REGULER, PREMIUM, atau LANGGANAN.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // ==================== SIMPAN KE DATABASE ====================
        try {
            System.out.println("🔍 [ParkInServlet] Mengecek plat: " + license);
            boolean existsInDB = vehicleDAO.existsByPlate(license);
            System.out.println("🔍 [ParkInServlet] Plat ditemukan di DB: " + existsInDB);

            if (!existsInDB) {
                System.out.println("🆕 [ParkInServlet] Menyimpan plat baru ke DB: " + license);
                Vehicle newVehicle = new Vehicle(license, type);
                boolean inserted = vehicleDAO.insertVehicle(newVehicle);
                if (!inserted) {
                    throw new RuntimeException("Gagal insert ke tabel vehicles");
                }
                System.out.println("✅ [ParkInServlet] Berhasil simpan ke DB");
            } else {
                System.out.println("ℹ️ [ParkInServlet] Plat sudah ada di DB, lewati insert");
            }
        } catch (Exception e) {
            System.err.println("💥 [ParkInServlet] ERROR saat simpan ke DB: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("msg", "❌ Gagal menyimpan kendaraan ke database.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }
        // ==========================================================

        // Daftarkan ke ParkingManager (di memori)
        if (!manager.isVehicleRegistered(license)) {
            manager.registerVehicle(license, type, ""); // ownerName diabaikan
        }

        // Buat objek kendaraan untuk parkir
        Vehicle vehicle;
        try {
            vehicle = new Vehicle(license, type);
        } catch (Exception e) {
            session.setAttribute("msg", "❌ Data kendaraan tidak valid.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // Proses parkir
        boolean success = manager.park(vehicle, spotType);

        if (success) {
            session.setAttribute("msg",
                    "✅ Kendaraan " + license + " berhasil parkir di spot " + spotType + "!");

            // Update session untuk tampilan
            java.util.List<java.util.Map<String, Object>> parkedVehicles =
                (java.util.List<java.util.Map<String, Object>>)
                    session.getAttribute("parkedVehicles");

            if (parkedVehicles == null) {
                parkedVehicles = new java.util.ArrayList<>();
            }

            java.util.Map<String, Object> row = new java.util.HashMap<>();
            row.put("plate", license);
            row.put("spot", spotType);
            row.put("subs", "LANGGANAN".equals(spotType));
            row.put("spotType", spotType);

            parkedVehicles.add(row);
            session.setAttribute("parkedVehicles", parkedVehicles);

        } else {
            if ("LANGGANAN".equals(spotType)) {
                session.setAttribute("msg",
                    "❌ Gagal parkir di LANGGANAN. Pastikan kendaraan memiliki langganan aktif.");
            } else {
                session.setAttribute("msg",
                    "❌ Gagal parkir. Tidak ada spot " + spotType + " yang tersedia.");
            }
        }

        session.setAttribute("activeVehicles", manager.getActiveVehicleCount());
        session.setAttribute("todayRevenue", manager.getTodayRevenue());

        response.sendRedirect("dashboard-petugas.jsp");
    }
}