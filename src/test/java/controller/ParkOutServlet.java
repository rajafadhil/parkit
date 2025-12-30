package controller;

import dao.ParkingSpotDAO;
import dao.VehicleDAO;
import dao.ParkingSessionDAO;
import model.Vehicle;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/ParkOutServlet")
public class ParkOutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String license = request.getParameter("licensePlate");
        if (license == null || license.trim().isEmpty()) {
            session.setAttribute("msg", "❌ Plat nomor wajib diisi.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        license = license.trim().toUpperCase();

        ParkingSpotDAO spotDAO = new ParkingSpotDAO();
        VehicleDAO vehicleDAO = new VehicleDAO();

        // === 1. Cari spot berdasarkan plat ===
        String spotId = findSpotByPlate(license);
        if (spotId == null) {
            session.setAttribute("msg", "❌ Kendaraan dengan plat " + license + " tidak ditemukan di area parkir.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // === 2. Ambil data spot dari DB ===
        ParkingSpotDAO.SpotData spotData = spotDAO.getSpotData(spotId);
        if (spotData == null || spotData.getEntryTime() == null) {
            session.setAttribute("msg", "❌ Data parkir tidak valid di database.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // === 3. Ambil tipe kendaraan ===
        String vehicleType = vehicleDAO.getVehicleTypeByPlate(license);
        if (vehicleType == null) {
            vehicleType = "MOBIL"; // fallback
        }

        // === 4. Ambil vehicle_id untuk simpan ke parking_sessions ===
        int vehicleId = vehicleDAO.getVehicleIdByPlate(license);
        if (vehicleId == -1) {
            session.setAttribute("msg", "❌ Data kendaraan tidak ditemukan.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // === 5. HITUNG TARIF (tanpa objek ParkingSpot) ===
        LocalDateTime entryTime = spotData.getEntryTime();
        LocalDateTime exitTime = LocalDateTime.now();
        long minutes = Duration.between(entryTime, exitTime).toMinutes();
        if (minutes < 0) minutes = 0;

        double fee = 0.0;
        String spotTypeDisplay = "REGULER";

        if ("premium".equalsIgnoreCase(spotData.getSpotType())) {
            spotTypeDisplay = "PREMIUM";
            if (minutes > 60) {
                long hours = (minutes + 59) / 60; // ceiling
                fee = "MOBIL".equals(vehicleType) ? (hours - 1) * 10000 : (hours - 1) * 5000;
            } else {
                fee = 0;
            }
        } else {
            long hours = (minutes + 59) / 60;
            fee = "MOBIL".equals(vehicleType) ? hours * 5000 : hours * 2000;
        }

        // === 6. SIMPAN KE PARKING_SESSIONS ===
        ParkingSessionDAO sessionDAO = new ParkingSessionDAO();
        boolean saved = sessionDAO.saveCompleteSession(vehicleId, spotId, entryTime, exitTime, fee);

        // === 7. UPDATE UI SESI ===
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd-MM-yy , HH:mm:ss", new Locale("id", "ID"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> transactions =
            (List<Map<String, Object>>) session.getAttribute("dailyTransactions");

        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        Map<String, Object> trx = new HashMap<>();
        trx.put("plate", license);
        trx.put("vehicleType", vehicleType);
        trx.put("spotType", spotTypeDisplay);
        trx.put("timeIn", sdf.format(java.sql.Timestamp.valueOf(entryTime)));
        trx.put("timeOut", sdf.format(new Date()));
        trx.put("fee", (int) fee);

        transactions.add(trx);
        session.setAttribute("dailyTransactions", transactions);

        // === 8. HAPUS DARI PARKED VEHICLES ===
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> parkedVehicles =
            (List<Map<String, Object>>) session.getAttribute("parkedVehicles");

        if (parkedVehicles != null) {
            Iterator<Map<String, Object>> it = parkedVehicles.iterator();
            while (it.hasNext()) {
                Map<String, Object> v = it.next();
                Object plateObj = v.get("plate");
                if (plateObj != null && license.equals(plateObj)) {
                    it.remove();
                    break;
                }
            }
            session.setAttribute("parkedVehicles", parkedVehicles);
        }

        // === 9. KOSONGKAN SPOT DI DATABASE ===
        boolean released = spotDAO.releaseSpot(spotId);
        if (!released) {
            System.err.println("⚠️ Gagal melepas spot " + spotId);
            // Tapi tetap lanjutkan — jangan hentikan proses
        }

        // === 10. NOTIFIKASI ===
        String msg = "✅ Kendaraan " + license + " keluar. Tarif: Rp " + (int) fee;
        if (!saved) {
            msg += " (⚠️ Riwayat gagal disimpan ke database)";
        }
        session.setAttribute("msg", msg);

        // === 11. REDIRECT (SELALU DIJALANKAN) ===
        response.sendRedirect("dashboard-petugas.jsp");
    }

    // === BANTUAN: Cari spot_id berdasarkan plat ===
    private String findSpotByPlate(String licensePlate) {
        String[] allSpots = {"R1","R2","R3","R4","R5","R6","R7","R8","R9","R10","P1","P2","P3","P4","P5"};
        ParkingSpotDAO dao = new ParkingSpotDAO();

        for (String id : allSpots) {
            ParkingSpotDAO.SpotData data = dao.getSpotData(id);
            if (data != null && licensePlate.equals(data.getLicensePlate())) {
                return id;
            }
        }
        return null;
    }
}