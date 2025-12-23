<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = (String) session.getAttribute("regError");
    String success = (String) session.getAttribute("regSuccess");
%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <title>Register - ParkIT</title>

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
            background: #f1f5f9;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .container {
            width: 1000px;
            max-width: 95%;
            background: #ffffff;
            border-radius: 20px;
            display: flex;
            overflow: hidden;
            box-shadow: 0 20px 40px rgba(0,0,0,0.15);
        }

        /* KIRI (SAMA LOGIN) */
        .left {
            width: 45%;
            background: linear-gradient(180deg, #2563eb, #3b82f6);
            color: white;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            text-align: center;
            padding: 40px;
        }

        .logo-box {
            width: 90px;
            height: 90px;
            background: white;
            border-radius: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 42px;
            font-weight: bold;
            color: #2563eb;
            margin-bottom: 20px;
        }

        .left h1 {
            font-size: 36px;
            margin-bottom: 10px;
        }

        .left p {
            font-size: 16px;
            opacity: 0.95;
        }

        /* KANAN */
        .right {
            width: 55%;
            padding: 50px;
            position: relative;
        }

        .help {
            position: absolute;
            top: 30px;
            right: 40px;
            color: #2563eb;
            font-weight: 600;
            text-decoration: none;
        }

        .right h2 {
            font-size: 32px;
            margin-bottom: 30px;
            color: #0f172a;
        }

        .input-group {
            margin-bottom: 18px;
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
            background: #2563eb;
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
        }

        .success {
            background: #dcfce7;
            color: #047857;
            padding: 12px;
            border-radius: 10px;
            margin-bottom: 15px;
        }

        .countdown {
            margin-top: 10px;
            font-weight: 600;
            color: #0f172a;
        }

        .bottom-text {
            margin-top: 25px;
            text-align: center;
        }

        .bottom-text a {
            color: #2563eb;
            font-weight: 600;
            text-decoration: none;
        }
    </style>
</head>

<body>

<div class="container">

    <!-- KIRI -->
    <div class="left">
        <div class="logo-box">P</div>
        <h1>ParkIT</h1>
        <p>Solusi Parkir Digital Modern & Efisien</p>
    </div>

    <!-- KANAN -->
    <div class="right">

        <a href="#" class="help">Need help?</a>

        <h2>Register</h2>

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
                    <input type="password" name="password" placeholder="Password" required>
                </div>

                <button type="submit" class="btn">REGISTER</button>
            </form>

            <div class="bottom-text">
                Sudah punya akun? <a href="login.jsp">Login di sini</a>
            </div>

        <% } %>

    </div>
</div>

</body>
</html>
