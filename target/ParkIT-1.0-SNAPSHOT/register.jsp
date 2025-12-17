<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = (String) session.getAttribute("regError");
    String success = (String) session.getAttribute("regSuccess");
%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <title>Daftar - ParkIT</title>

    <% if (success != null) { %>
        <meta http-equiv="refresh" content="3;url=login.jsp">
    <% } %>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', sans-serif;
        }

        body {
            min-height: 100vh;
            background: linear-gradient(135deg, #1e293b, #0f766e);
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .auth-container {
            width: 900px;
            max-width: 95%;
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 45px rgba(0,0,0,0.25);
            display: flex;
            overflow: hidden;
        }

        /* BAGIAN KIRI */
        .auth-left {
            width: 45%;
            background: linear-gradient(135deg, #0f766e, #1e293b);
            color: white;
            padding: 50px 35px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            text-align: center;
        }

        .auth-left img {
            width: 80px;
            margin-bottom: 20px;
        }

        .auth-left h1 {
            font-size: 34px;
            margin-bottom: 10px;
            letter-spacing: 1px;
        }

        .auth-left p {
            font-size: 16px;
            opacity: 0.95;
            line-height: 1.6;
        }

        /* BAGIAN KANAN */
        .auth-right {
            width: 55%;
            padding: 45px 40px;
        }

        .auth-right h2 {
            color: #1e293b;
            font-size: 26px;
            margin-bottom: 20px;
        }

        .input-group {
            margin-bottom: 16px;
        }

        .input-group input {
            width: 100%;
            padding: 14px;
            border-radius: 10px;
            border: 1px solid #cbd5e1;
            font-size: 16px;
        }

        .btn {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #0f766e, #14b8a6);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 18px;
            font-weight: 600;
            cursor: pointer;
            margin-top: 10px;
        }

        .error {
            background: #fee2e2;
            color: #b91c1c;
            padding: 12px;
            border-radius: 10px;
            margin-bottom: 15px;
            font-weight: 500;
        }

        .success {
            background: #dcfce7;
            color: #047857;
            padding: 12px;
            border-radius: 10px;
            margin-bottom: 15px;
            font-weight: 500;
        }

        .countdown {
            color: #1e293b;
            font-weight: 600;
            margin-top: 10px;
        }

        .auth-right a {
            color: #0f766e;
            text-decoration: none;
            font-weight: 600;
        }
    </style>
</head>

<body>

<div class="auth-container">

    <!-- KIRI: BRANDING -->
    <div class="auth-left">
        <img src="logo.png" alt="ParkIT Logo" onerror="this.style.display='none'">
        <h1>ParkIT</h1>
        <p>Solusi Parkir Digital<br>Modern & Efisien</p>
    </div>

    <!-- KANAN: FORM -->
    <div class="auth-right">
        <h2>Daftar Akun Petugas</h2>

        <% if (error != null) { %>
            <div class="error"><%= error %></div>
            <% session.removeAttribute("regError"); %>
        <% } %>

        <% if (success != null) { %>
            <div class="success"><%= success %></div>
            <div class="countdown">
                Anda akan dialihkan ke halaman login dalam 3 detik...
            </div>
            <% session.removeAttribute("regSuccess"); %>
        <% } else { %>
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

            <p style="margin-top: 20px; text-align: center;">
                Sudah punya akun?
                <a href="login.jsp">Login di sini</a>
            </p>
        <% } %>
    </div>

</div>

</body>
</html>
