import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.security.MessageDigest;
import java.math.BigInteger;
import java.sql.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null || username.trim().isEmpty() || password.isEmpty()) {
            request.getSession().setAttribute("loginError", "Username dan password wajib diisi.");
            response.sendRedirect("login.jsp");
            return;
        }

        username = username.trim();

        Connection conn = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/parkit?useSSL=false&serverTimezone=UTC",
                "root", ""
            );

            rs = conn.createStatement().executeQuery(
                "SELECT password, role FROM users WHERE username = '" + username + "'"
            );

            if (rs.next()) {
                String storedHash = rs.getString("password");
                String inputHash = hashPassword(password);
                String role = rs.getString("role");

                if (storedHash.equals(inputHash)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    session.setAttribute("role", role != null ? role : "PENGGUNA");

                    if ("PETUGAS".equals(role)) {
                        response.sendRedirect("dashboard-petugas.jsp");
                    } else {
                        response.sendRedirect("dashboard-user.jsp");
                    }
                    return;
                }
            }

            request.getSession().setAttribute("loginError", "Username atau password salah.");
            response.sendRedirect("login.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("loginError", "Terjadi kesalahan sistem.");
            response.sendRedirect("login.jsp");
        } finally {
            try {
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return String.format("%064x", new BigInteger(1, md.digest(password.getBytes())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}