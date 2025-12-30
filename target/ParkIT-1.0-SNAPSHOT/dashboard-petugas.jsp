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

    // ✅ Aman dari ClassCastException
    Object parkedObj = session.getAttribute("parkedVehicles");
    java.util.List<java.util.Map<String, Object>> parkedVehicles = null;
    if (parkedObj instanceof java.util.List) {
        parkedVehicles = (java.util.List<java.util.Map<String, Object>>) parkedObj;
    }
    if (parkedVehicles == null) {
        parkedVehicles = new java.util.ArrayList<>();
    }
%>

<!DOCTYPE html>
<html lang="id">
<head>
<meta charset="UTF-8">
<title>ParkIT - Dashboard Petugas</title>

<style>
* {
    box-sizing: border-box;
    font-family: 'Segoe UI', sans-serif;
}

body {
    margin: 0;
    background: #f5f7fb;
    color: #1f2937;
}

.app {
    display: flex;
    min-height: 100vh;
}

.sidebar {
    width: 260px;
    background: #0f172a;
    color: #fff;
    padding: 25px 20px;
}

.profile {
    text-align: center;
    margin-bottom: 40px;
}

.profile img {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    object-fit: cover;
    margin-bottom: 10px;
}

.profile h4 {
    margin: 5px 0;
}

.profile span {
    font-size: 13px;
    color: #cbd5f5;
}

.menu a {
    display: block;
    padding: 12px 15px;
    border-radius: 10px;
    color: #e5e7eb;
    text-decoration: none;
    margin-bottom: 10px;
    font-weight: 500;
}

.menu a:hover {
    background: #1e293b;
}

.content {
    flex: 1;
}

.header {
    padding: 25px 40px;
}

.header h1 {
    margin: 0;
}

.stats {
    display: flex;
    gap: 20px;
    margin-top: 15px;
}

.stat-card {
    background: #fff;
    border-radius: 12px;
    padding: 20px;
    width: 220px;
    box-shadow: 0 8px 20px rgba(0,0,0,0.05);
}

.stat-number {
    font-size: 28px;
    font-weight: bold;
    color: #2563eb;
}

.notification {
    background: #dcfce7;
    color: #166534;
    padding: 12px;
    border-radius: 8px;
    margin: 20px 40px;
    font-weight: 600;
}

.board {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 25px;
    padding: 0 40px 40px;
}

.column {
    background: #f9fafb;
    border-radius: 16px;
    padding: 15px;
}

.column h3 {
    margin-bottom: 15px;
    padding-bottom: 10px;
    border-bottom: 3px solid #e5e7eb;
}

.card {
    background: #fff;
    border-radius: 12px;
    padding: 18px;
    margin-bottom: 15px;
    box-shadow: 0 6px 15px rgba(0,0,0,0.05);
}

.form-group {
    margin-bottom: 12px;
}

label {
    font-size: 14px;
    font-weight: 600;
}

input, select {
    width: 100%;
    padding: 9px;
    border-radius: 8px;
    border: 1px solid #d1d5db;
}

.btn {
    margin-top: 10px;
    width: 100%;
    border: none;
    padding: 10px;
    border-radius: 8px;
    font-weight: bold;
    cursor: pointer;
    color: #fff;
    background: #2563eb;
}

.btn.out { background: #dc2626; }
.btn.subscribe { background: #16a34a; }

table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 10px;
}

th, td {
    padding: 10px;
    text-align: center;
    border-bottom: 1px solid #e5e7eb;
}

th {
    background: #f3f4f6;
}

.footer {
    text-align: center;
    padding: 20px;
    color: #6b7280;
}
</style>
</head>

<body>

<div class="app">

    <!-- SIDEBAR -->
    <aside class="sidebar">
        <div class="profile">
            <!-- ✅ Perbaiki URL avatar: HAPUS SPASI -->
            <img src="https://i.pravatar.cc/150" alt="Profile">
            <h4><%= username %></h4>
            <span>petugas@parkit.com</span>
        </div>

        <nav class="menu">
            <a href="#">📊 Dashboard</a>
            <a href="laporan.jsp">📄 Laporan</a>
            <!-- ✅ Logout via servlet -->
            <a href="login.jsp">🚪 Logout</a>
        </nav>
    </aside>

    <!-- CONTENT -->
    <main class="content">

        <div class="header">
            <h1>Dashboard Petugas</h1>

            <div class="stats">
                <div class="stat-card">
                    <div class="stat-number"><%= activeVehicles %></div>
                    Kendaraan Aktif
                </div>
                <div class="stat-card">
                    <div class="stat-number">Rp<%= String.format("%.0f", todayRevenue) %></div>
                    Pendapatan Hari Ini
                </div>
            </div>
        </div>

        <% if (msg != null) { %>
        <div class="notification"><%= msg %></div>
        <% } %>

        <div class="board">

            <div class="column">
                <h3>🅿️ Parkir Masuk</h3>
                <div class="card">
                    <form action="ParkInServlet" method="post">
                        <div class="form-group">
                            <label>Plat Nomor</label>
                            <input type="text" name="licensePlate" required>
                        </div>
                        <div class="form-group">
                            <label>Jenis Kendaraan</label>
                            <select name="vehicleType">
                                <option value="MOTOR">Motor</option>
                                <option value="MOBIL">Mobil</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Jenis Spot</label>
                            <select name="spotType">
                                <option value="REGULER">Reguler</option>
                                <option value="PREMIUM">Premium</option>
                                <option value="LANGGANAN">Langganan</option>
                            </select>
                        </div>
                        <button class="btn">Parkir Sekarang</button>
                    </form>
                </div>
            </div>

            <div class="column">
                <h3>🚗 Proses</h3>

                <div class="card">
                    <form action="ParkOutServlet" method="post">
                        <label>Plat Nomor</label>
                        <input type="text" name="licensePlate" required>
                        <button class="btn out">Keluar</button>
                    </form>
                </div>

                <div class="card">
                    <form action="SubscribeServlet" method="post">
                        <label>Plat Nomor</label>
                        <input type="text" name="licensePlate" required>
                        <label>Durasi</label>
                        <select name="duration">
                            <option value="1">1 Bulan</option>
                            <option value="6">6 Bulan</option>
                            <option value="12">1 Tahun</option>
                        </select>
                        <button class="btn subscribe">Daftar Langganan</button>
                    </form>
                </div>
            </div>

            <div class="column">
                <h3>📋 Kendaraan Parkir</h3>
                <div class="card">
                    <table>
                        <thead>
                            <tr>
                                <th>Plat</th>
                                <th>Spot</th>
                                <th>Subs</th>
                                <th>Aksi</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% if (!parkedVehicles.isEmpty()) {
                            for (java.util.Map<String, Object> v : parkedVehicles) {
                                String plate = (String) v.get("plate");
                                String spot = (String) v.get("spot"); // Gunakan spot ID
                                Boolean subs = (Boolean) v.get("subs");
                                String spotType = (String) v.get("spotType");
                        %>
                        <tr>
                            <td><%= plate %></td>
                            <td><%= spot %></td>
                            <td><%= subs != null && subs ? "Ya" : "Tidak" %></td>
                            <td>
                                <% if ("PREMIUM".equals(spotType) || (subs != null && subs)) { %>
                                    <button class="btn"
                                        onclick="washVehicle('<%= plate %>')">
                                        Washable
                                    </button>
                                <% } else { %>-<% } %>
                            </td>
                        </tr>
                        <% } } else { %>
                        <tr><td colspan="4">Belum ada kendaraan</td></tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>

        <div class="footer">
            &copy; 2025 ParkIT — Solusi Parkir Digital Masa Kini
        </div>

    </main>
</div>

<script>
// Auto uppercase plat
document.querySelectorAll('input[name="licensePlate"]').forEach(i => {
    i.addEventListener('input', () => i.value = i.value.toUpperCase());
});
</script>

<script>
function washVehicle(plate) {
    if (plate) {
        // ✅ Tampilkan notifikasi sesuai permintaan lama
        if (plate.startsWith("P")) {
            alert("Kendaraan premium sedang dicuci!");
        } else {
            alert("Kendaraan langganan sedang dicuci!");
        }
    }
}
</script>

</body>
</html>