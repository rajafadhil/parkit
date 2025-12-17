<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = (String) session.getAttribute("loginError");
    if (error != null) {
        session.removeAttribute("loginError");
    }
%>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <title>Login - ParkIT</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        body {
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            background: #f5f7fb;
        }

        .container {
            width: 900px;
            height: 520px;
            display: flex;
            background: #fff;
            border-radius: 14px;
            box-shadow: 0 15px 40px rgba(0,0,0,0.15);
            overflow: hidden;
        }

        .branding {
            width: 45%;
            background: linear-gradient(135deg, #2563eb, #3b82f6);
            color: white;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            padding: 40px;
            text-align: center;
        }

        .logo {
            width: 80px;
            height: 80px;
            background: white;
            color: #2563eb;
            border-radius: 16px;
            font-size: 40px;
            font-weight: bold;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 20px;
        }

        .branding h1 {
            font-size: 32px;
            margin-bottom: 10px;
        }

        .branding p {
            font-size: 16px;
            opacity: 0.9;
        }

        .login-form {
            width: 55%;
            padding: 50px;
            display: flex;
            justify-content: center;
            align-items: center;
            position: relative;
        }

        .form-box {
            width: 100%;
            max-width: 360px;
        }

        .help-link {
            position: absolute;
            top: 25px;
            right: 35px;
            font-size: 14px;
        }

        .help-link a {
            color: #2563eb;
            text-decoration: none;
            font-weight: 500;
        }

        .form-box h2 {
            font-size: 28px;
            margin-bottom: 30px;
            color: #1f2937;
            text-align: center;
        }

        .input-field input {
            width: 100%;
            padding: 14px;
            margin-bottom: 16px;
            border-radius: 8px;
            border: 1px solid #d1d5db;
            font-size: 15px;
        }

        .forgot-link {
            text-align: right;
            margin-bottom: 20px;
            font-size: 14px;
        }

        .forgot-link a {
            color: #2563eb;
            text-decoration: none;
        }

        .btn-login {
            width: 100%;
            padding: 14px;
            background: #2563eb;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
        }

        .btn-login:hover {
            background: #1e40af;
        }

        .register-link {
            margin-top: 22px;
            text-align: center;
            font-size: 14px;
        }

        .register-link a {
            color: #2563eb;
            text-decoration: none;
            font-weight: 500;
        }

        .error {
            background: #fee2e2;
            color: #b91c1c;
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 20px;
            text-align: center;
        }
    </style>
</head>
<body>

<div class="container">

    <!-- LEFT BRANDING -->
    <div class="branding">
        <div class="logo">P</div>
        <h1>ParkIT</h1>
        <p>Solusi Parkir Digital Modern & Efisien</p>
    </div>

    <!-- RIGHT LOGIN FORM -->
    <div class="login-form">

        <div class="help-link">
            <a href="#">Need help?</a>
        </div>

        <div class="form-box">
            <h2>Login</h2>

            <% if (error != null) { %>
                <div class="error"><%= error %></div>
            <% } %>

            <form action="LoginServlet" method="post">
                <div class="input-field">
                    <input type="text" name="username" placeholder="Username" required>
                </div>

                <div class="input-field">
                    <input type="password" name="password" placeholder="Password" required>
                </div>

                <div class="forgot-link">
                    <a href="#">Forgot password?</a>
                </div>

                <button type="submit" class="btn-login">LOGIN</button>
            </form>

            <div class="register-link">
                Belum punya akun? <a href="register.jsp">Daftar di sini</a>
            </div>
        </div>

    </div>
</div>

</body>
</html>
