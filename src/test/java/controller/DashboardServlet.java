package controller;

import dao.ParkingSpotDAO;
import dao.VehicleDAO;
import dao.SubscriptionDAO;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.*;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    // ✅ Sesuaikan dengan spot yang benar-benar ada di database Anda
    private static final String[] VALID_SPOTS = {
        "R1", "R2", "R3", "R4", "R5", "R6", "R7", "R8", "R9", "R10", // Regular
        "P1", "P2", "P3", "P4", "P5"  // Premium
        // Tidak ada "S1" karena tidak ada di database Anda
    };

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String username = (String) session.getAttribute("username");
        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<Map<String, Object>> parkedVehicles = new ArrayList<>();

        try {
            ParkingSpotDAO spotDAO = new ParkingSpotDAO();
            VehicleDAO vehicleDAO = new VehicleDAO();
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO();

            for (String spotId : VALID_SPOTS) {
                ParkingSpotDAO.SpotData data = spotDAO.getSpotData(spotId);
                
                if (data != null && 
                    data.getLicensePlate() != null && 
                    !data.getLicensePlate().trim().isEmpty() &&
                    data.getEntryTime() != null) {

                    String plate = data.getLicensePlate().trim();

                    // ✅ Perbaikan: gunakan equalsIgnoreCase()
                    String spotTypeRaw = data.getSpotType();
                    boolean isPremium = false;
                    if (spotTypeRaw != null) {
                        isPremium = "premium".equalsIgnoreCase(spotTypeRaw.trim());
                    }

                    boolean hasActiveSub = false;
                    int vehicleId = vehicleDAO.getVehicleIdByPlate(plate);
                    if (vehicleId != -1) {
                        hasActiveSub = subscriptionDAO.hasActiveSubscription(vehicleId);
                    }

                    boolean washable = isPremium || hasActiveSub;

                    Map<String, Object> v = new HashMap<>();
                    v.put("plate", plate);
                    v.put("spot", spotId);
                    v.put("spotType", isPremium ? "PREMIUM" : "REGULER");
                    v.put("subs", hasActiveSub);
                    v.put("isPremium", isPremium);
                    v.put("washable", washable);

                    parkedVehicles.add(v);
                }
            }

            session.setAttribute("parkedVehicles", parkedVehicles);
            session.setAttribute("activeVehicles", parkedVehicles.size());

            request.getRequestDispatcher("dashboard-petugas.jsp").forward(request, response);

        } catch (Exception e) { // Biarkan Exception umum untuk debugging awal
            System.err.println("💥 [DashboardServlet] Error: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("msg", "❌ Gagal memuat dashboard.");
            response.sendRedirect("dashboard-petugas.jsp"); // tetap di halaman, bukan login
        }
    }
}