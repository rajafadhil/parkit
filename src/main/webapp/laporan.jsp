<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Ambil data transaksi harian
    java.util.List<java.util.Map<String, Object>> transactions =
        (java.util.List<java.util.Map<String, Object>>) session.getAttribute("dailyTransactions");
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

/* ===== LAYOUT ===== */
.app {
    display: flex;
    min-height: 100vh;
}

/* ===== SIDEBAR ===== */
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

/* ===== CONTENT ===== */
.content {
    flex: 1;
    padding: 30px 40px;
}

/* ===== HEADER ===== */
.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.header h1 {
    margin: 0;
}

/* ===== BUTTON ===== */
.btn {
    border: none;
    padding: 10px 18px;
    border-radius: 8px;
    font-weight: bold;
    cursor: pointer;
    color: #fff;
    background: #2563eb;
}

/* ===== TABLE ===== */
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

/* ===== PRINT ===== */
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
            <img src="https://i.pravatar.cc/150" alt="Profile">
            <h4><%= username %></h4>
            <span>petugas@parkit.com</span>
        </div>

        <nav class="menu">
            <a href="dashboard-petugas.jsp">📊 Dashboard</a>
            <a href="#">🅿️ Slot Parkir</a>
            <a href="#">📋 Data</a>
            <a href="laporan.jsp">📄 Laporan</a>
            <a href="logout.jsp">🚪 Logout</a>
        </nav>
    </aside>

    <!-- CONTENT -->
    <main class="content">

        <div class="header">
            <h1>📄 Laporan Transaksi Harian</h1>
            <button class="btn" onclick="window.print()">💾 Save as PDF</button>
        </div>

        <div class="card">
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
                    if (transactions != null && !transactions.isEmpty()) {
                        int no = 1;
                        for (java.util.Map<String, Object> t : transactions) {
                %>
                    <tr>
                        <td><%= no++ %></td>
                        <td><%= t.get("plate") %></td>
                        <td><%= t.get("vehicleType") %></td>
                        <td><%= t.get("spotType") %></td>
                        <td><%= t.get("timeIn") %></td>
                        <td><%= t.get("timeOut") %></td>
                        <td>Rp<%= t.get("fee") %></td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="7">Belum ada transaksi hari ini</td>
                    </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>

        <div class="footer">
            &copy; 2025 ParkIT — Laporan Harian
        </div>

    </main>
</div>

</body>
</html>
