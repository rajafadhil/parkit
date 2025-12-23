package controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.security.MessageDigest;
import java.math.BigInteger;
import java.sql.*;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validasi input
        if (username == null || email == null || password == null ||
            username.trim().isEmpty() || email.trim().isEmpty() || password.length() < 6) {
            HttpSession session = request.getSession();
            session.setAttribute("regError", "Semua field wajib diisi. Password minimal 6 karakter.");
            response.sendRedirect("register.jsp");
            return;
        }

        username = username.trim();
        email = email.trim().toLowerCase();
        String role = "PETUGAS"; // Semua akun = petugas

        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null; // ✅ Tambahkan deklarasi ResultSet

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3307/parkit?useSSL=false&serverTimezone=UTC",
                "root", ""
            );

            // Cek duplikasi
            checkStmt = conn.prepareStatement("SELECT 1 FROM users WHERE username = ? OR email = ?");
            checkStmt.setString(1, username);
            checkStmt.setString(2, email);
            rs = checkStmt.executeQuery(); // ✅ Eksekusi query → dapatkan ResultSet

            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("regError", "Username atau email sudah terdaftar.");
                response.sendRedirect("register.jsp");
                return;
            }

            // Hash password
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String hashed = String.format("%064x", new BigInteger(1, md.digest(password.getBytes())));

            // Simpan ke database
            insertStmt = conn.prepareStatement("INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)");
            insertStmt.setString(1, username);
            insertStmt.setString(2, email);
            insertStmt.setString(3, hashed);
            insertStmt.setString(4, role);
            insertStmt.executeUpdate();

            // ✅ SET SESI SUKSES
            HttpSession session = request.getSession();
            request.getSession().setAttribute("regSuccess", "Pendaftaran berhasil! Silakan login.");
response.sendRedirect("register.jsp"); // ✅ Harus ke register.jsp

        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("regError", "Terjadi kesalahan sistem. Pastikan database aktif.");
            response.sendRedirect("register.jsp");
        } finally {
            try {
                if (rs != null) rs.close(); // ✅ Sekarang aman
                if (insertStmt != null) insertStmt.close();
                if (checkStmt != null) checkStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}