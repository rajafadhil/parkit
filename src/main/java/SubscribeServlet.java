// SubscribeServlet.java — TANPA package & TANPA import internal
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/SubscribeServlet")
public class SubscribeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        
        // Ambil ParkingManager dari application context (ServletContext)
        ParkingManager manager = (ParkingManager) getServletContext().getAttribute("manager");
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

        // Tambahkan langganan
        LocalDate start = LocalDate.now();
        LocalDate expiry = start.plusMonths(duration);
        manager.registerSubscription(license, start, expiry);

        session.setAttribute("msg", "✅ Langganan untuk " + license + " aktif hingga " + expiry + "!");
        response.sendRedirect("dashboard-petugas.jsp");
    }
}