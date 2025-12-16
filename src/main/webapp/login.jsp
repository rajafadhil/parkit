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
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: Arial, sans-serif; }
        body { background: linear-gradient(to right, #355c7d, #6c5b7b, #c06c84); height: 100vh; display: flex; justify-content: center; align-items: center; }
        .login-container { width: 380px; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 20px rgba(0,0,0,0.2); text-align: center; }
        .login-container h2 { margin: 20px 0; color: #333; }
        .input-field input { width: 100%; padding: 12px; margin: 10px 0; border: 1px solid #ddd; border-radius: 5px; font-size: 16px; }
        .btn-login { width: 100%; padding: 12px; background: #3498db; color: white; border: none; font-size: 17px; border-radius: 5px; cursor: pointer; font-weight: bold; }
        .error { color: #e11d48; background: #fee2e2; padding: 12px; border-radius: 8px; margin: 15px 0; }
    </style>
</head>
<body>
    <div class="login-container">
        <h2>ParkIT</h2>
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
            <button type="submit" class="btn-login">LOGIN</button>
        </form>
        <p style="margin-top: 20px;"><a href="register.jsp" style="color: #3498db; text-decoration: none;">Belum punya akun? Daftar di sini</a></p>
    </div>
</body>
</html>