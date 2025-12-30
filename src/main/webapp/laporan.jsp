<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Transaksi parkir harian
    java.util.List<java.util.Map<String, Object>> transactions =
        (java.util.List<java.util.Map<String, Object>>) session.getAttribute("dailyTransactions");

    // Transaksi langganan
    java.util.List<java.util.Map<String, Object>> subscriptions =
        (java.util.List<java.util.Map<String, Object>>) session.getAttribute("subscriptionTransactions");
%>

<!DOCTYPE html>
<html lang="id">
<head>
<meta charset="UTF-8">
<title>ParkIT - Laporan Harian</title>

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
    margin-bottom: 10px;
}

.menu a {
    display: block;
    padding: 12px 15px;
    border-radius: 10px;
    color: #e5e7eb;
    text-decoration: none;
    margin-bottom: 10px;
}

.menu a:hover {
    background: #1e293b;
}

.content {
    flex: 1;
    padding: 30px 40px;
}

.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.btn {
    border: none;
    padding: 10px 18px;
    border-radius: 8px;
    font-weight: bold;
    cursor: pointer;
    color: #fff;
    background: #2563eb;
}

.card {
    background: #fff;
    margin-top: 25px;
    border-radius: 14px;
    padding: 20px;
    box-shadow: 0 8px 20px rgba(0,0,0,0.05);
}

table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 10px;
}

th, td {
    padding: 12px;
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

@media print {
    .sidebar, .btn {
        display: none;
    }
}
</style>
</head>

<body>
<div class="app">

<!-- SIDEBAR -->
<aside class="sidebar">
    <div class="profile">
        <img src="https://i.pravatar.cc/150">
        <h4><%= username %></h4>
        <span>petugas@parkit.com</span>
    </div>

    <nav class="menu">
        <a href="dashboard-petugas.jsp">📊 Dashboard</a>
        <a href="laporan.jsp">📄 Laporan</a>
        <a href="login.jsp">🚪 Logout</a>
    </nav>
</aside>

<!-- CONTENT -->
<main class="content">

<div class="header">
    <h1>📄 Laporan Harian</h1>
    <button class="btn" onclick="window.print()">💾 Save as PDF</button>
</div>

<!-- ================= TABEL 1 : TRANSAKSI PARKIR ================= -->
<div class="card">
<h2>🚗 Transaksi Parkir Harian</h2>

<table>
<thead>
<tr>
    <th>No</th>
    <th>Plat Nomor</th>
    <th>Jenis</th>
    <th>Spot</th>
    <th>Jam Masuk</th>
    <th>Jam Keluar</th>
    <th>Biaya</th>
</tr>
</thead>

<tbody>
<%
    int totalParkir = 0;

    if (transactions != null && !transactions.isEmpty()) {
        int no = 1;
        for (java.util.Map<String, Object> t : transactions) {
            int fee = Integer.parseInt(String.valueOf(t.get("fee")));
            totalParkir += fee;
%>
<tr>
    <td><%= no++ %></td>
    <td><%= t.get("plate") %></td>
    <td><%= t.get("vehicleType") %></td>
    <td><%= t.get("spotType") %></td>
    <td><%= t.get("timeIn") %></td>
    <td><%= t.get("timeOut") %></td>
    <td>Rp <%= fee %></td>
</tr>
<%
        }
%>
<tr>
    <td colspan="6" style="text-align:right;font-weight:bold;">TOTAL PARKIR</td>
    <td style="font-weight:bold;">Rp <%= totalParkir %></td>
</tr>
<%
    } else {
%>
<tr>
    <td colspan="7">Belum ada transaksi parkir</td>
</tr>
<%
    }
%>
</tbody>
</table>
</div>

<!-- ================= TABEL 2 : LANGGANAN ================= -->
<div class="card">
<h2>📌 Pendaftaran Langganan</h2>

<table>
<thead>
<tr>
    <th>No</th>
    <th>Plat Nomor</th>
    <th>Jenis Kendaraan</th>
    <th>Tanggal Mulai</th>
    <th>Tanggal Berakhir</th>
    <th>Biaya</th>
</tr>
</thead>

<tbody>
<%
    int totalLangganan = 0;

    if (subscriptions != null && !subscriptions.isEmpty()) {
        int no = 1;
        for (java.util.Map<String, Object> s : subscriptions) {
            int fee = Integer.parseInt(String.valueOf(s.get("fee")));
            totalLangganan += fee;
%>
<tr>
    <td><%= no++ %></td>
    <td><%= s.get("plate") %></td>
    <td><%= s.get("vehicleType") %></td>
    <td><%= s.get("startDate") %></td>
    <td><%= s.get("endDate") %></td>
    <td>Rp <%= fee %></td>
</tr>
<%
        }
%>
<tr>
    <td colspan="5" style="text-align:right;font-weight:bold;">TOTAL LANGGANAN</td>
    <td style="font-weight:bold;">Rp <%= totalLangganan %></td>
</tr>
<%
    } else {
%>
<tr>
    <td colspan="6">Belum ada pendaftaran langganan</td>
</tr>
<%
    }
%>
</tbody>
</table>
</div>

<div class="footer">
    &copy; 2025 ParkIT — Sistem Parkir Terintegrasi
</div>

</main>
</div>
</body>
</html>
