package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3307/parkit?useSSL=false&serverTimezone=Asia/Jakarta";

    private static final String USER = "root";
    private static final String PASS = ""; // kosong kalau default XAMPP

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver Loaded");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver NOT FOUND");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
