package controller;

import dao.SubscriptionDAO;
import dao.VehicleDAO;
import dao.ParkingSpotDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Duration;

@WebServlet("/CheckStatusServlet")
public class CheckStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String license = request.getParameter("licensePlate");
        if (license == null || license.trim().isEmpty()) {
            request.setAttribute("result",
                "<div class='result-row'><span class='value' style='color:#e74c3c;'>❌ Plat nomor wajib diisi.</span></div>");
            request.getRequestDispatcher("status.jsp").forward(request, response);
            return;
        }

        license = license.trim().toUpperCase();

        VehicleDAO vehicleDAO = new VehicleDAO();
        SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
        ParkingSpotDAO spotDAO = new ParkingSpotDAO();

        // === 1. Cek apakah kendaraan terdaftar ===
        int vehicleId = vehicleDAO.getVehicleIdByPlate(license);
        if (vehicleId == -1) {
            request.setAttribute("result",
                "<div class='result-row'><span class='label'>Plat:</span><span class='value'>" + license + "</span></div>" +
                "<div class='result-row'><span class='label'>Langganan:</span><span class='value'>Tidak Terdaftar ❌</span></div>" +
                "<div class='result-row'><span class='label'>Status:</span><span class='value'>Tidak Dikenali</span></div>");
            request.getRequestDispatcher("status.jsp").forward(request, response);
            return;
        }

        String vehicleType = vehicleDAO.getVehicleTypeByPlate(license);
        if (vehicleType == null) vehicleType = "MOBIL";

        // === 2. Cek langganan aktif ===
        boolean hasActiveSubscription = subscriptionDAO.hasActiveSubscription(vehicleId);

        // === 3. Cari parkir aktif di parking_spots ===
        String[] allSpots = {"R1","R2","R3","R4","R5","R6","R7","R8","R9","R10","P1","P2","P3","P4","P5"};
        ParkingSpotDAO.SpotData currentSpot = null;
        String foundSpotId = null;

        for (String spotId : allSpots) {
            ParkingSpotDAO.SpotData data = spotDAO.getSpotData(spotId);
            if (data != null && license.equals(data.getLicensePlate()) && data.getEntryTime() != null) {
                currentSpot = data;
                foundSpotId = spotId;
                break;
            }
        }

        // === 4. Bangun hasil ===
        StringBuilder result = new StringBuilder();
        result.append("<div class='result-row'><span class='label'>Plat:</span><span class='value'>")
              .append(license)
              .append("</span></div>");

        result.append("<div class='result-row'><span class='label'>Langganan:</span><span class='value'>")
              .append(hasActiveSubscription ? "Aktif ✅" : "Tidak Aktif ❌")
              .append("</span></div>");

        if (currentSpot != null) {
            result.append("<div class='result-row'><span class='label'>Status:</span><span class='value'>Sedang Parkir</span></div>");
            result.append("<div class='result-row'><span class='label'>Spot:</span><span class='value'>")
                  .append(foundSpotId)
                  .append("</span></div>");
            result.append("<div class='result-row'><span class='label'>Masuk:</span><span class='value'>")
                  .append(currentSpot.getEntryTime())
                  .append("</span></div>");

            // Hitung perkiraan tarif
            LocalDateTime now = LocalDateTime.now();
            long minutes = Duration.between(currentSpot.getEntryTime(), now).toMinutes();
            long hours = Math.max(1, (minutes + 59) / 60); // ceiling

            double estFee = 0;
            boolean eligibleForWash = hasActiveSubscription;

            if (hasActiveSubscription) {
                estFee = 0;
            } else {
                if ("MOTOR".equals(vehicleType)) {
                    estFee = hours * 5000; // Sesuaikan tarif Anda
                } else {
                    estFee = hours * 10000;
                }

                // Jika premium → cuci gratis
                if ("premium".equalsIgnoreCase(currentSpot.getSpotType())) {
                    eligibleForWash = true;
                    // Tarif premium bisa 2x, sesuaikan kebijakan
                    estFee *= 1.5;
                }
            }

            result.append("<div class='result-row'><span class='label'>Perkiraan Tarif:</span><span class='value'>Rp ")
                  .append((long) estFee)
                  .append("</span></div>");

            result.append("<div class='result-row'><span class='label'>Cuci Gratis:</span><span class='value'>")
                  .append(eligibleForWash ? "Ya <span class='wash-badge'>GRATIS</span>" : "Tidak")
                  .append("</span></div>");

        } else {
            result.append("<div class='result-row'><span class='label'>Status:</span><span class='value'>Tidak Sedang Parkir</span></div>");
            if (hasActiveSubscription) {
                result.append("<div class='result-row'><span class='label'>Cuci Gratis:</span><span class='value'>Ya <span class='wash-badge'>GRATIS</span> (Langganan Aktif)</span></div>");
            }
        }

        request.setAttribute("result", result.toString());
        request.getRequestDispatcher("status.jsp").forward(request, response);
    }
}