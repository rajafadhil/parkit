<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String msg = (String) session.getAttribute("msg");
    if (msg != null) {
        session.removeAttribute("msg");
    }

    Integer activeVehicles = (Integer) session.getAttribute("activeVehicles");
    Double todayRevenue = (Double) session.getAttribute("todayRevenue");
    if (activeVehicles == null) activeVehicles = 0;
    if (todayRevenue == null) todayRevenue = 0.0;

    java.util.List<java.util.Map<String, Object>> parkedVehicles =
        (java.util.List<java.util.Map<String, Object>>) session.getAttribute("parkedVehicles");
%>

<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ParkIT - Dashboard Petugas</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background: linear-gradient(135deg, #0c4a6e, #1d4ed8);
            color: #fff;
            min-height: 100vh;
            padding: 20px;
        }

        .container {
            background-color: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.3);
            padding: 40px;
            max-width: 1000px;
            margin: auto;
        }

        h1 {
            font-size: 2.5rem;
            margin-bottom: 20px;
        }

        .notification {
            background: #dcfce7;
            color: #059669;
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 20px;
            text-align: center;
            font-weight: bold;
        }

        .stats {
            display: flex;
            justify-content: space-around;
            margin-bottom: 30px;
        }

        .stat-number {
            font-size: 2.2rem;
            color: #4dabf7;
            font-weight: bold;
        }

        .form-section {
            background: rgba(0,0,0,0.2);
            padding: 25px;
            border-radius: 15px;
            margin: 30px 0;
        }

        .form-section h3 {
            color: #4dabf7;
            margin-bottom: 15px;
        }

        .form-group {
            margin-bottom: 15px;
            text-align: left;
        }

        input, select {
            width: 100%;
            padding: 10px;
            border-radius: 8px;
            border: none;
        }

        .btn {
            background: #3498db;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 8px;
            cursor: pointer;
            font-weight: bold;
        }

        .btn.out { background: #e74c3c; }
        .btn.subscribe { background: #2ecc71; }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }

        th, td {
            padding: 14px;
            text-align: center;
            border-bottom: 1px solid rgba(255,255,255,0.2);
        }

        th {
            background: rgba(255,255,255,0.15);
        }

        .footer {
            margin-top: 40px;
            text-align: center;
            color: #a0d2ff;
        }
    </style>
</head>

<body>

<div class="container">
    <h1>👮 Dashboard Petugas - Selamat datang, <strong><%= username %></strong>!</h1>

    <% if (msg != null) { %>
        <div class="notification"><%= msg %></div>
    <% } %>

    <div class="stats">
        <div>
            <div class="stat-number"><%= activeVehicles %></div>
            Kendaraan Aktif
        </div>
        <div>
            <div class="stat-number">Rp<%= String.format("%.0f", todayRevenue) %></div>
            Pendapatan Hari Ini
        </div>
    </div>

    <!-- PARKIR MASUK -->
    <div class="form-section">
        <h3>🅿️ Parkir Masuk</h3>
        <form action="ParkInServlet" method="post">
            <div class="form-group">
                <label>Plat Nomor</label>
                <input type="text" name="licensePlate" required>
            </div>
            <div class="form-group">
                <label>Jenis Kendaraan</label>
                <select name="vehicleType" required>
                    <option value="MOTOR">Motor</option>
                    <option value="MOBIL">Mobil</option>
                </select>
            </div>
            <div class="form-group">
                <label>Jenis Spot</label>
                <select name="spotType" required>
                    <option value="REGULER">Reguler</option>
                    <option value="PREMIUM">Premium</option>
                    <option value="LANGGANAN">Langganan</option>
                </select>
            </div>
            <button type="submit" class="btn">Parkir Sekarang</button>
        </form>
    </div>

    <!-- PARKIR KELUAR (TIDAK HILANG) -->
    <div class="form-section">
        <h3>🚗 Parkir Keluar</h3>
        <form action="ParkOutServlet" method="post">
            <div class="form-group">
                <label>Plat Nomor</label>
                <input type="text" name="licensePlate" required>
            </div>
            <button type="submit" class="btn out">Keluar Sekarang</button>
        </form>
    </div>

    <!-- LANGGANAN (TIDAK HILANG) -->
    <div class="form-section">
        <h3>📅 Daftarkan Langganan</h3>
        <form action="SubscribeServlet" method="post">
            <div class="form-group">
                <label>Plat Nomor</label>
                <input type="text" name="licensePlate" required>
            </div>
            <div class="form-group">
                <label>Masa Berlaku</label>
                <select name="duration" required>
                    <option value="1">1 Bulan</option>
                    <option value="6">6 Bulan</option>
                    <option value="12">1 Tahun</option>
                </select>
            </div>
            <button type="submit" class="btn subscribe">Daftar Langganan</button>
        </form>
    </div>

    <!-- TABEL KENDARAAN PARKIR (TAMBAHAN SAJA) -->
    <div class="form-section">
        <h3>📋 Kendaraan Sedang Parkir</h3>

        <table>
            <thead>
                <tr>
                    <th>Plat</th>
                    <th>Spot Parkir</th>
                    <th>Subs</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
            <% if (parkedVehicles != null && !parkedVehicles.isEmpty()) {
                for (java.util.Map<String, Object> v : parkedVehicles) {
                    String plate = (String) v.get("plate");
                    String spot = (String) v.get("spotType");
                    Boolean subs = (Boolean) v.get("subs");
            %>
                <tr>
                    <td><%= plate %></td>
                    <td><%= spot %></td>
                    <td><%= subs ? "Ya" : "Tidak" %></td>
                    <td>
                        <% if ("PREMIUM".equals(spot) || Boolean.TRUE.equals(subs)) { %>
                            <button class="btn">Washable</button>
                        <% } else { %>
                            -
                        <% } %>
                    </td>
                </tr>
            <% } } else { %>
                <tr>
                    <td colspan="4">Belum ada kendaraan parkir</td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>

<script>
    document.querySelectorAll('input[name="licensePlate"]').forEach(i => {
        i.addEventListener('input', () => i.value = i.value.toUpperCase());
    });
</script>

<div class="footer">
    &copy; 2025 ParkIT — Solusi Parkir Digital Masa Kini.
</div>

</body>
</html>
