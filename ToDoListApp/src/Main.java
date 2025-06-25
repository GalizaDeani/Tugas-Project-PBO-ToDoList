import java.sql.Connection;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author galiz
 */
public class Main {
    public static void main(String[] args) {
        Connection conn = (Connection) DatabaseConnection.getConnection();
        if (conn != null) {
            System.out.println("Tes Koneksi Berhasil!");
        } else {
            System.out.println("Tes Koneksi Gagal!");
        }
    }
}
