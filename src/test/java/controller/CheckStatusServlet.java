package controller;

import model.ParkingSession;
import util.ParkingManager;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;

@WebServlet("/CheckStatusServlet")
public class CheckStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String license = request.getParameter("licensePlate");
            if (license == null || license.trim().isEmpty()) {
                request.setAttribute(
                    "result",
                    "<div class='result-row'><span class='value' style='color:#e74c3c;'>❌ Plat nomor wajib diisi.</span></div>"
                );
                request.getRequestDispatcher("status.jsp").forward(request, response);
                return;
            }
            license = license.trim().toUpperCase();

            ParkingManager manager = (ParkingManager) getServletContext().getAttribute("manager");
            if (manager == null) {
                request.setAttribute(
                    "result",
                    "<div class='result-row'><span class='value' style='color:#e74c3c;'>❌ Sistem belum siap.</span></div>"
                );
                request.getRequestDispatcher("status.jsp").forward(request, response);
                return;
            }

            StringBuilder result = new StringBuilder();
            result.append("<div class='result-row'><span class='label'>Plat:</span><span class='value'>")
                  .append(license)
                  .append("</span></div>");

            // Cek status langganan
            boolean hasActiveSubscription = manager.isEligibleSubscription(license);
            result.append("<div class='result-row'><span class='label'>Langganan:</span><span class='value'>")
                  .append(hasActiveSubscription ? "Aktif ✅" : "Tidak Aktif ❌")
                  .append("</span></div>");

            // Cek sesi parkir aktif
            List<ParkingSession> activeSessions = manager.getLog().getActiveSessions();
            ParkingSession current = null;
            for (ParkingSession s : activeSessions) {
                if (license.equals(s.getLicensePlate())) {
                    current = s;
                    break;
                }
            }

            if (current != null) {
                result.append("<div class='result-row'><span class='label'>Status:</span><span class='value'>Sedang Parkir</span></div>");
                result.append("<div class='result-row'><span class='label'>Spot:</span><span class='value'>")
                      .append(current.getSpotId())
                      .append("</span></div>");
                result.append("<div class='result-row'><span class='label'>Masuk:</span><span class='value'>")
                      .append(current.getEntryTime())
                      .append("</span></div>");

                // ===============================
                // HITUNG TARIF
                // ===============================
                LocalDateTime now = LocalDateTime.now();
                long minutes = Duration.between(current.getEntryTime(), now).toMinutes();

                // Minimal 1 jam
                long hours = Math.max(1, (minutes + 59) / 60);

                String type = current.getVehicleType();
                double estFee = 0;
                boolean eligibleForWash = false;

                if (hasActiveSubscription) {
                    estFee = 0;
                    eligibleForWash = true;
                } else {
                    if ("MOTOR".equals(type)) {
                        estFee = hours * 3000;
                    } else {
                        estFee = hours * 5000;
                    }

                    // Premium = 2x tarif + cuci gratis
                    if (current.getSpotId().startsWith("P")) {
                        estFee *= 2;
                        eligibleForWash = true;
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

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute(
                "result",
                "<div class='result-row'><span class='value' style='color:#e74c3c;'>❌ Terjadi kesalahan sistem.</span></div>"
            );
            request.getRequestDispatcher("status.jsp").forward(request, response);
        }
    }
}
