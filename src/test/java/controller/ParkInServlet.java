package controller;

import dao.ParkingSpotDAO;
import dao.VehicleDAO;
import dao.SubscriptionDAO;
import model.Vehicle;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet("/ParkInServlet")
public class ParkInServlet extends HttpServlet {

    private static final String[] REGULAR_SPOTS = {"R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10"};
    private static final String[] PREMIUM_SPOTS = {"P1", "P2", "P3", "P4", "P5"};

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String license = request.getParameter("licensePlate");
        String type = request.getParameter("vehicleType");
        String spotTypeParam = request.getParameter("spotType");

        if (license == null || type == null || spotTypeParam == null ||
            license.trim().isEmpty() || type.trim().isEmpty()) {
            session.setAttribute("msg", "❌ Semua field wajib diisi.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        license = license.trim().toUpperCase();
        type = type.trim().toUpperCase();
        String spotType = spotTypeParam.trim().toUpperCase();

        // ========== 1. CEK / SIMPAN VEHICLE ==========
        VehicleDAO vehicleDAO = new VehicleDAO();
        int vehicleId = vehicleDAO.getVehicleIdByPlate(license);

        if (vehicleId == -1) {
            // Belum ada → daftarkan
            Vehicle vehicle = new Vehicle(license, type);
            if (!vehicleDAO.insertVehicle(vehicle)) {
                session.setAttribute("msg", "❌ Gagal menyimpan kendaraan ke database.");
                response.sendRedirect("dashboard-petugas.jsp");
                return;
            }
            vehicleId = vehicleDAO.getVehicleIdByPlate(license);
            if (vehicleId == -1) {
                session.setAttribute("msg", "❌ Gagal mendapatkan ID kendaraan.");
                response.sendRedirect("dashboard-petugas.jsp");
                return;
            }
        }

        // ========== 2. CEK LANGGANAN ==========
        if ("LANGGANAN".equals(spotType)) {
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
            if (!subscriptionDAO.hasActiveSubscription(vehicleId)) {
                session.setAttribute("msg", "❌ Kendaraan tidak memiliki langganan aktif.");
                response.sendRedirect("dashboard-petugas.jsp");
                return;
            }
        }

        // ========== 3. CARI SPOT KOSONG ==========
        String spotId = findAvailableSpot(spotType);
        if (spotId == null) {
            session.setAttribute("msg", "❌ Tidak ada spot tersedia.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // ========== 4. TENTUKAN TIPE SPOT UNTUK DATABASE (SESUAI ENUM) ==========
        String dbSpotType = "REGULER";
        if ("PREMIUM".equals(spotType) || "LANGGANAN".equals(spotType)) {
            dbSpotType = "PREMIUM";
        }

        // ========== 5. PARKIR (PAKAI DAO) ==========
        ParkingSpotDAO spotDAO = new ParkingSpotDAO();
        boolean success = spotDAO.occupySpot(spotId, license, dbSpotType); // ✅ Pakai versi occupySpot yang sudah diperbaiki

        if (!success) {
            session.setAttribute("msg", "❌ Gagal memproses parkir. Spot mungkin sedang digunakan.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // ========== 6. UPDATE UI ==========
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> parkedVehicles =
            (List<Map<String, Object>>) session.getAttribute("parkedVehicles");

        if (parkedVehicles == null) {
            parkedVehicles = new ArrayList<>();
        }

        Map<String, Object> row = new HashMap<>();
        row.put("plate", license);
        row.put("spot", spotId);
        row.put("subs", "LANGGANAN".equals(spotType));
        row.put("spotType", spotType);

        parkedVehicles.add(row);
        session.setAttribute("parkedVehicles", parkedVehicles);
        session.setAttribute("msg", "✅ Kendaraan " + license + " berhasil parkir di " + spotId);

        response.sendRedirect("dashboard-petugas.jsp");
    }

    // ========== CARI SPOT KOSONG ==========
    private String findAvailableSpot(String spotType) {
        ParkingSpotDAO spotDAO = new ParkingSpotDAO();
        String[] spots = "REGULER".equals(spotType) ? REGULAR_SPOTS : PREMIUM_SPOTS;

        for (String id : spots) {
            ParkingSpotDAO.SpotData data = spotDAO.getSpotData(id);
            if (data != null && data.getLicensePlate() == null && data.getEntryTime() == null) {
                return id;
            }
        }
        return null;
    }
}