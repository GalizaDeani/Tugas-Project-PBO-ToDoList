/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.Date;
/**
 *
 * @author galiz
 */
public class Tugas {
    private int id;
    private String judul;
    private String deskripsi;
    private User user;
    private Kategori kategori;
    private Prioritas prioritas;
    private Date tanggal;

    public Tugas(int id, String judul, String deskripsi, User user, Kategori kategori, Prioritas prioritas, Date tanggal) {
        this.id = id;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.user = user;
        this.kategori = kategori;
        this.prioritas = prioritas;
        this.tanggal = tanggal;
    }

    // Getter dan Setter lengkap bisa ditambahkan di sini
    public int getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public User getUser() {
        return user;
    }

    public Kategori getKategori() {
        return kategori;
    }

    public Prioritas getPrioritas() {
        return prioritas;
    }

    public Date getTanggal() {
        return tanggal;
    }
}