// ParkInServlet.java — TANPA PACKAGE & TANPA IMPORT INTERNAL
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/ParkInServlet")
public class ParkInServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        
        // Ambil atau inisialisasi ParkingManager di application scope
        ParkingManager manager = (ParkingManager) getServletContext().getAttribute("manager");
        if (manager == null) {
            manager = new ParkingManager();
            getServletContext().setAttribute("manager", manager);
        }

        // Ambil parameter
        String license = request.getParameter("licensePlate");
        String type = request.getParameter("vehicleType");
        String spotType = request.getParameter("spotType");

        // Validasi input
        if (license == null || type == null || spotType == null ||
            license.trim().isEmpty() || type.trim().isEmpty()) {
            session.setAttribute("msg", "❌ Semua field wajib diisi.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // Normalisasi input
        license = license.trim().toUpperCase();
        type = type.trim().toUpperCase();
        spotType = spotType.trim().toUpperCase();

        // Validasi jenis kendaraan
        if (!"MOTOR".equals(type) && !"MOBIL".equals(type)) {
            session.setAttribute("msg", "❌ Jenis kendaraan harus MOTOR atau MOBIL.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // Validasi jenis spot
        if (!"REGULER".equals(spotType) && !"PREMIUM".equals(spotType) && !"LANGGANAN".equals(spotType)) {
            session.setAttribute("msg", "❌ Jenis spot harus REGULER, PREMIUM, atau LANGGANAN.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // Daftarkan kendaraan jika belum ada
        if (!manager.isVehicleRegistered(license)) {
            manager.registerVehicle(license, type, "Web User");
        }

        // Buat objek Vehicle
        Vehicle vehicle;
        try {
            vehicle = new Vehicle(license, type, "Web User");
        } catch (Exception e) {
            session.setAttribute("msg", "❌ Data kendaraan tidak valid.");
            response.sendRedirect("dashboard-petugas.jsp");
            return;
        }

        // Parkir kendaraan
        boolean success = manager.park(vehicle, spotType);

        if (success) {
            session.setAttribute("msg", "✅ Kendaraan " + license + " berhasil parkir di spot " + spotType + "!");

            // ======================= TAMBAHAN UNTUK UPDATE TABEL =======================
            java.util.List<java.util.Map<String, Object>> parkedVehicles =
                (java.util.List<java.util.Map<String, Object>>) session.getAttribute("parkedVehicles");

            if (parkedVehicles == null) {
                parkedVehicles = new java.util.ArrayList<>();
            }

            java.util.Map<String, Object> row = new java.util.HashMap<>();
            row.put("plate", license);
            row.put("spot", spotType);
            row.put("subs", "LANGGANAN".equals(spotType));
            row.put("spotType", spotType);

            parkedVehicles.add(row);

            session.setAttribute("parkedVehicles", parkedVehicles);
            // ===========================================================================
        } else {
            if ("LANGGANAN".equals(spotType)) {
                session.setAttribute("msg",
                    "❌ Gagal parkir di LANGGANAN. Pastikan kendaraan sudah terdaftar sebagai pelanggan dengan langganan aktif.");
            } else {
                session.setAttribute("msg",
                    "❌ Gagal parkir. Tidak ada spot " + spotType + " yang tersedia.");
            }
        }

        // Update statistik
        session.setAttribute("activeVehicles", manager.getActiveVehicleCount());
        session.setAttribute("todayRevenue", manager.getTodayRevenue());

        response.sendRedirect("dashboard-petugas.jsp");
    }
}
