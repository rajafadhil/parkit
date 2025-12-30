package controller;

import dao.UserDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.security.MessageDigest;
import java.math.BigInteger;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();

        // ================= VALIDASI =================
        if (username == null || email == null || password == null ||
            username.trim().isEmpty() ||
            email.trim().isEmpty() ||
            password.length() < 6) {

            session.setAttribute(
                "regError",
                "Semua field wajib diisi. Password minimal 6 karakter."
            );
            response.sendRedirect("register.jsp");
            return;
        }

        username = username.trim();
        email = email.trim().toLowerCase();
        String role = "PETUGAS";

        try {
            UserDAO userDAO = new UserDAO();

            // ================= CEK DUPLIKASI =================
            if (userDAO.existsByUsernameOrEmail(username, email)) {
                session.setAttribute(
                    "regError",
                    "Username atau email sudah terdaftar."
                );
                response.sendRedirect("register.jsp");
                return;
            }

            // ================= HASH PASSWORD =================
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String hashedPassword = String.format(
                "%064x",
                new BigInteger(1, md.digest(password.getBytes()))
            );

            // ================= SIMPAN USER =================
            boolean inserted = userDAO.insertUser(
                username, email, hashedPassword, role
            );

            if (!inserted) {
                throw new RuntimeException("Insert user gagal");
            }

            session.setAttribute(
                "regSuccess",
                "Pendaftaran berhasil! Silakan login."
            );
            response.sendRedirect("register.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute(
                "regError",
                "Terjadi kesalahan sistem. Pastikan database aktif."
            );
            response.sendRedirect("register.jsp");
        }
    }
}
