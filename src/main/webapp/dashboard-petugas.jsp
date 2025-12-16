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
%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="webtoken" content="width=device-width, initial-scale=1.0">
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
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: flex-start;
            padding: 20px;
            position: relative;
        }

        .container {
            background-color: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.3);
            padding: 40px;
            text-align: center;
            max-width: 900px;
            width: 90%;
            border: 1px solid rgba(255, 255, 255, 0.2);
            margin-top: 60px;
        }

        h1 {
            color: #fff;
            font-size: 3rem;
            margin-bottom: 15px;
            text-shadow: 2px 2px 8px rgba(0, 0, 0, 0.5);
        }

        .welcome {
            font-size: 1.3rem;
            color: #a0d2ff;
            margin-bottom: 30px;
            font-weight: 500;
        }

        .notification {
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-weight: 600;
            text-align: center;
            background: #dcfce7;
            color: #059669;
        }

        .stats {
            display: flex;
            justify-content: space-around;
            margin: 40px 0;
            flex-wrap: wrap;
        }

        .stat-item {
            text-align: center;
            padding: 15px;
        }

        .stat-number {
            font-size: 2.5rem;
            font-weight: bold;
            color: #4dabf7;
        }

        .stat-label {
            font-size: 1rem;
            color: #a0d2ff;
        }

        .form-section {
            background: rgba(0,0,0,0.2);
            padding: 25px;
            border-radius: 15px;
            margin: 25px 0;
        }

        .form-section h3 {
            margin-bottom: 15px;
            color: #4dabf7;
        }

        .form-group {
            margin: 12px 0;
            text-align: left;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            color: #a0d2ff;
        }

        .form-group input, .form-group select {
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
            margin-top: 10px;
        }

        .btn.out {
            background: #e74c3c;
        }

        .btn.subscribe {
            background: #2ecc71;
        }

        .footer {
            margin-top: 40px;
            color: #a0d2ff;
            font-size: 1rem;
            text-align: center;
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
            <div class="stat-item">
                <div class="stat-number"><%= activeVehicles %></div>
                <div class="stat-label">Kendaraan Aktif</div>
            </div>
            <div class="stat-item">
                <div class="stat-number">Rp<%= String.format("%.0f", todayRevenue) %></div>
                <div class="stat-label">Pendapatan Hari Ini</div>
            </div>
        </div>

        <div class="form-section">
            <h3>🅿️ Parkir Masuk</h3>
            <form action="ParkInServlet" method="post">
                <div class="form-group">
                    <label>Plat Nomor</label>
                    <input type="text" name="licensePlate" placeholder="Contoh: B1234ABC" required>
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

        <div class="form-section">
            <h3>🚗 Parkir Keluar</h3>
            <form action="ParkOutServlet" method="post">
                <div class="form-group">
                    <label>Plat Nomor</label>
                    <input type="text" name="licensePlate" placeholder="Masukkan plat" required>
                </div>
                <button type="submit" class="btn out">Keluar Sekarang</button>
            </form>
        </div>
        
        <div class="form-section">
            <h3>📅 Daftarkan Langganan</h3>
            <form action="SubscribeServlet" method="post">
                <div class="form-group">
                    <label>Plat Nomor</label>
                    <input type="text" name="licensePlate" placeholder="Plat kendaraan" required>
                </div>
                <div class="form-group">
                    <label>Masa Berlaku (Bulan)</label>
                    <select name="duration" required>
                        <option value="1">1 Bulan</option>
                        <option value="6">6 Bulan</option>
                        <option value="12">1 Tahun</option>
                    </select>
                </div>
                <button type="submit" class="btn subscribe">Daftar Langganan</button>
            </form>
        </div>
    </div>

    <script>
        // Otomatis ubah plat nomor jadi kapital
        document.addEventListener('DOMContentLoaded', function() {
            const licenseInputs = document.querySelectorAll('input[name="licensePlate"]');
            licenseInputs.forEach(input => {
                input.addEventListener('input', function() {
                    this.value = this.value.toUpperCase();
                });
            });
        });
    </script>

    <div class="footer">
        &copy; 2025 ParkIT — Sistem Manajemen Parkir Terminal
    </div>

</body>
</html>