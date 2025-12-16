<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = (String) session.getAttribute("regError");
    String success = (String) session.getAttribute("regSuccess");
    // Jangan hapus session di sini — biarkan sampai tampil
%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <title>Daftar - ParkIT</title>
    <% if (success != null) { %>
        <!-- Auto-redirect setelah 3 detik -->
        <meta http-equiv="refresh" content="3;url=login.jsp">
    <% } %>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Segoe UI', sans-serif; }
        body { background: linear-gradient(135deg, #0c4a6e, #1d4ed8); min-height: 100vh; display: flex; align-items: center; justify-content: center; }
        .register-card { background: white; border-radius: 20px; box-shadow: 0 15px 40px rgba(0,0,0,0.25); width: 100%; max-width: 450px; padding: 40px; text-align: center; }
        .register-card h2 { color: #0c4a6e; margin-bottom: 20px; font-size: 28px; }
        .input-group { margin: 16px 0; text-align: left; }
        .input-group input { width: 100%; padding: 14px; border: 1px solid #ddd; border-radius: 12px; font-size: 16px; }
        .btn { width: 100%; padding: 14px; background: #0c4a6e; color: white; border: none; border-radius: 12px; font-size: 18px; font-weight: 600; cursor: pointer; }
        .error { color: #e11d48; background: #fee2e2; padding: 12px; border-radius: 10px; margin: 15px 0; font-weight: 500; }
        .success { color: #059669; background: #dcfce7; padding: 12px; border-radius: 10px; margin: 15px 0; font-weight: 500; }
        .countdown { color: #0c4a6e; font-weight: 600; margin-top: 10px; }
    </style>
</head>
<body>
    <div class="register-card">
        <h2>Daftar Akun Petugas ParkIT</h2>
        
        <% if (error != null) { %>
            <div class="error"><%= error %></div>
            <% session.removeAttribute("regError"); %>
        <% } %>
        
        <% if (success != null) { %>
            <div class="success"><%= success %></div>
            <div class="countdown">Anda akan dialihkan ke halaman login dalam 3 detik...</div>
            <% session.removeAttribute("regSuccess"); %>
        <% } else { %>
            <!-- Tampilkan form hanya jika tidak ada pesan sukses -->
            <form method="POST" action="RegisterServlet">
                <div class="input-group">
                    <input type="text" name="username" placeholder="Username" required>
                </div>
                <div class="input-group">
                    <input type="email" name="email" placeholder="Email" required>
                </div>
                <div class="input-group">
                    <input type="password" name="password" placeholder="Password (min 6 karakter)" required minlength="6">
                </div>
                <button type="submit" class="btn">Daftar sebagai Petugas</button>
            </form>
            <p style="margin-top: 20px;">
                <a href="login.jsp" style="color: #1d4ed8; text-decoration: none;">Sudah punya akun? Login di sini</a>
            </p>
        <% } %>
    </div>
</body>
</html>