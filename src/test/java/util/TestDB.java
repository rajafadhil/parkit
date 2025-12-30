package util;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        try {
            var conn = util.DBConnection.getConnection();
            System.out.println("🎉 Koneksi BERHASIL!");
            conn.close();
        } catch (SQLException e) {
            System.err.println("💥 Koneksi GAGAL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}