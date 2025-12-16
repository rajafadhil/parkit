// ParkOutServlet.java — TANPA PACKAGE & TANPA IMPORT INTERNAL
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/ParkOutServlet")
public class ParkOutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        
        // Ambil ParkingManager dari application scope
        ParkingManager manager = (ParkingManager) getServletContext().getAttribute("manager");
        if (manager == null) {
            session.setAttribute("msg", "❌ Sistem parkir belum diinisialisasi.");
            response.sendRedirect("dashboard-petugas.jsp"); // ✅ DIPERBAIKI
            return;
        }

        String license = request.getParameter("licensePlate");
        if (license == null || license.trim().isEmpty()) {
            session.setAttribute("msg", "❌ Plat nomor wajib diisi.");
            response.sendRedirect("dashboard-petugas.jsp"); // ✅ DIPERBAIKI
            return;
        }

        license = license.toUpperCase().trim();
        ParkingSession sessionResult = manager.unpark(license); // ✅ gunakan metode unpark

        if (sessionResult != null) {
            session.setAttribute("msg", "✅ Kendaraan " + license + " keluar. Tarif: Rp " + (int) sessionResult.getFee());
        } else {
            session.setAttribute("msg", "❌ Kendaraan tidak ditemukan atau tidak sedang parkir.");
        }

        // Update statistik
        session.setAttribute("activeVehicles", manager.getActiveVehicleCount());
        session.setAttribute("todayRevenue", manager.getTodayRevenue());

        response.sendRedirect("dashboard-petugas.jsp"); // ✅ DIPERBAIKI
    }
}