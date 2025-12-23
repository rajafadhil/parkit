package controller;

import model.ParkingSession;
import util.ParkingManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/ParkOutServlet")
public class ParkOutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        ParkingManager manager =
                (ParkingManager) getServletContext().getAttribute("manager");

        if (manager == null) {
            session.setAttribute("msg", "❌ Sistem parkir belum diinisialisasi.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        String license = request.getParameter("licensePlate");
        if (license == null || license.trim().isEmpty()) {
            session.setAttribute("msg", "❌ Plat nomor wajib diisi.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        license = license.toUpperCase().trim();

        // ===== FORMAT WAKTU =====
        java.text.SimpleDateFormat sdf =
            new java.text.SimpleDateFormat(
                "EEEE dd-MM-yy , HH:mm:ss",
                new java.util.Locale("id", "ID")
            );

        String spotType = "-";
        String timeIn = null;

        java.util.List<java.util.Map<String, Object>> parkedVehicles =
            (java.util.List<java.util.Map<String, Object>>) session.getAttribute("parkedVehicles");

        // ===== AMBIL DATA SEBELUM DIHAPUS =====
        if (parkedVehicles != null) {
            for (java.util.Map<String, Object> v : parkedVehicles) {
                if (license.equals(v.get("plate"))) {
                    spotType = String.valueOf(v.get("spotType"));
                    timeIn = (String) v.get("timeIn"); // bisa null
                    break;
                }
            }
        }

        // ===== JIKA TIME IN NULL → ISI WAKTU SEKARANG =====
        if (timeIn == null) {
            timeIn = sdf.format(new java.util.Date());
        }

        ParkingSession sessionResult = manager.unpark(license);

        if (sessionResult != null) {

            session.setAttribute(
                "msg",
                "✅ Kendaraan " + license + " keluar. Tarif: Rp " + (int) sessionResult.getFee()
            );

            // ===== SIMPAN LAPORAN =====
            java.util.List<java.util.Map<String, Object>> transactions =
                (java.util.List<java.util.Map<String, Object>>) session.getAttribute("dailyTransactions");

            if (transactions == null) {
                transactions = new java.util.ArrayList<>();
            }

            java.util.Map<String, Object> trx = new java.util.HashMap<>();
            trx.put("plate", license);
            trx.put("vehicleType", sessionResult.getVehicleType());
            trx.put("spotType", spotType);
            trx.put("timeIn", timeIn);
            trx.put("timeOut", sdf.format(new java.util.Date()));
            trx.put("fee", (int) sessionResult.getFee());

            transactions.add(trx);
            session.setAttribute("dailyTransactions", transactions);

            // ===== HAPUS DARI PARKED =====
            if (parkedVehicles != null) {
                java.util.Iterator<java.util.Map<String, Object>> it = parkedVehicles.iterator();
                while (it.hasNext()) {
                    java.util.Map<String, Object> v = it.next();
                    if (license.equals(v.get("plate"))) {
                        it.remove();
                        break;
                    }
                }
                session.setAttribute("parkedVehicles", parkedVehicles);
            }

        } else {
            session.setAttribute("msg", "❌ Kendaraan tidak ditemukan.");
        }

        session.setAttribute("activeVehicles", manager.getActiveVehicleCount());
        session.setAttribute("todayRevenue", manager.getTodayRevenue());

        response.sendRedirect("dashboard-petugas.jsp");
    }
}
