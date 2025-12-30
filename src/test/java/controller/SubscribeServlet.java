package controller;

import dao.SubscriptionDAO;
import dao.VehicleDAO;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.time.LocalDate;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/SubscribeServlet")
public class SubscribeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        VehicleDAO vehicleDAO = new VehicleDAO();
        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();

        String license = request.getParameter("licensePlate");
        String durationStr = request.getParameter("duration");

        // Validasi input
        if (license == null || license.trim().isEmpty() || durationStr == null) {
            session.setAttribute("msg", "❌ Data langganan tidak lengkap.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        license = license.trim().toUpperCase();
        int duration;
        try {
            duration = Integer.parseInt(durationStr);
        } catch (NumberFormatException e) {
            session.setAttribute("msg", "❌ Durasi langganan tidak valid.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // === 1. Pastikan kendaraan ADA di tabel `vehicles` ===
        if (!vehicleDAO.existsByPlate(license)) {
            // Coba cari di database langsung via ID
            int vehicleId = vehicleDAO.getVehicleIdByPlate(license);
            if (vehicleId == -1) {
                session.setAttribute("msg", "❌ Kendaraan " + license + " tidak ditemukan di database. Pastikan kendaraan sudah diparkir atau terdaftar.");
                response.sendRedirect("dashboard-petugas.jsp");
                return;
            }
        }

        // === 2. Ambil vehicleId dengan pasti ===
        int vehicleId = vehicleDAO.getVehicleIdByPlate(license);
        if (vehicleId <= 0) {
            session.setAttribute("msg", "❌ ID kendaraan tidak valid untuk plat: " + license);
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // === 3. Cek apakah langganan sudah ada (opsional) ===
        if (subscriptionDAO.hasActiveSubscription(vehicleId)) {
            session.setAttribute("msg", "⚠️ Kendaraan " + license + " sudah memiliki langganan aktif.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // === 4. Hitung tanggal langganan ===
        LocalDate start = LocalDate.now();
        LocalDate expiry = start.plusMonths(duration);

        // === 5. Simpan ke database ===
        System.out.println("🔍 [SubscribeServlet] Mencoba simpan langganan: vehicleId=" + vehicleId + ", start=" + start + ", expiry=" + expiry);
        boolean saved = subscriptionDAO.addSubscription(vehicleId, start, expiry);

        if (!saved) {
            session.setAttribute("msg", "❌ Gagal menyimpan langganan ke database. Cek log untuk detail error.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // === 6. UI & Notifikasi ===
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> subs =
            (List<Map<String, Object>>) session.getAttribute("subscriptionTransactions");

        if (subs == null) {
            subs = new ArrayList<>();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd-MM-yyyy, HH:mm:ss", new Locale("id", "ID"));
        Map<String, Object> trx = new HashMap<>();
        trx.put("plate", license);
        trx.put("vehicleType", "LANGGANAN");
        trx.put("startDate", sdf.format(new Date()));
        trx.put("endDate", expiry.toString()); // typo fixed: was "expiry"
        trx.put("fee", 1000000);

        subs.add(trx);
        session.setAttribute("subscriptionTransactions", subs);

        Double revenue = (Double) session.getAttribute("todayRevenue");
        if (revenue == null) revenue = 0.0;
        session.setAttribute("todayRevenue", revenue + 1000000);

        session.setAttribute("msg", "✅ Langganan untuk " + license + " aktif hingga " + expiry + "!");
        response.sendRedirect("dashboard-petugas.jsp");
    }
}