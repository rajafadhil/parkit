package util;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("✅ BERHASIL: Koneksi ke database 'parkit' berhasil!");
            conn.close();
        } catch (SQLException e) {
            System.err.println("❌ GAGAL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}