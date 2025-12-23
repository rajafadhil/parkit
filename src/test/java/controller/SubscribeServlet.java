package controller;

import util.ParkingManager;

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
        
        // Ambil ParkingManager dari application context
        ParkingManager manager =
                (ParkingManager) getServletContext().getAttribute("manager");

        if (manager == null) {
            manager = new ParkingManager();
            getServletContext().setAttribute("manager", manager);
        }

        String license = request.getParameter("licensePlate");
        String durationStr = request.getParameter("duration");

        if (license == null || license.trim().isEmpty() || durationStr == null) {
            session.setAttribute("msg", "❌ Data langganan tidak lengkap.");
            response.sendRedirect("home.jsp");
            return;
        }

        license = license.toUpperCase().trim();
        int duration = Integer.parseInt(durationStr);

        // ================= LOGIKA ASLI (TIDAK DIUBAH) =================
        LocalDate start = LocalDate.now();
        LocalDate expiry = start.plusMonths(duration);
        manager.registerSubscription(license, start, expiry);
        // ===============================================================

        // ================= TAMBAHAN UNTUK LAPORAN =====================
        List<Map<String, Object>> subs =
            (List<Map<String, Object>>) session.getAttribute("subscriptionTransactions");

        if (subs == null) {
            subs = new ArrayList<>();
        }

        SimpleDateFormat sdf =
            new SimpleDateFormat(
                "EEEE dd-MM-yyyy, HH:mm:ss",
                new Locale("id","ID")
            );

        Map<String, Object> trx = new HashMap<>();
        trx.put("plate", license);
        trx.put("vehicleType", "LANGGANAN");
        trx.put("startDate", sdf.format(new Date()));
        trx.put("endDate", expiry.toString());
        trx.put("fee", 1000000);

        subs.add(trx);
        session.setAttribute("subscriptionTransactions", subs);

        // TAMBAH KE PENDAPATAN HARIAN
        Double revenue = (Double) session.getAttribute("todayRevenue");
        if (revenue == null) revenue = 0.0;
        session.setAttribute("todayRevenue", revenue + 1000000);
        // ===============================================================

        session.setAttribute(
            "msg",
            "✅ Langganan untuk " + license + " aktif hingga " + expiry + "!"
        );

        response.sendRedirect("dashboard-petugas.jsp");
    }
}
