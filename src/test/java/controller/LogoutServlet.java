package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/logout") // 👈 Lebih konsisten dengan nama URL (kecil semua)
public class LogoutServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Hancurkan sesi user
        request.getSession().invalidate();
        
        // Arahkan ke halaman login (bukan index.jsp, kecuali memang login di situ)
        response.sendRedirect("login.jsp");
    }
}