/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author galiz
 */
public class MainFrame extends JFrame{
    private JTextField txtJudul, txtCari;
    private JTextArea txtDeskripsi;
    private JComboBox<String> cmbKategori, cmbPrioritas, cmbStatus;
    private JSpinner spinnerDeadline;
    private JButton btnSimpan, btnUpdate, btnHapus, btnCari;
    private JTable tabelTugas;
    private DefaultTableModel tabelModel;
    private int selectedId = -1;

    public MainFrame() {
        setTitle("✨ To-Do List Galiza Nur ✨");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color pinkSoft = new Color(255, 240, 245);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 14);
        setBackground(pinkSoft);

        JPanel panelInput = new JPanel(new GridLayout(7, 2, 10, 5));
        panelInput.setBackground(pinkSoft);
        TitledBorder borderInput = BorderFactory.createTitledBorder("Form Tugas Harian");
        borderInput.setTitleFont(labelFont);
        panelInput.setBorder(borderInput);

        txtJudul = new JTextField();
        txtDeskripsi = new JTextArea(3, 20);
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);
        cmbKategori = new JComboBox<>(new String[]{"Pekerjaan", "Akademik", "Pribadi", "Kesehatan"});
        cmbPrioritas = new JComboBox<>(new String[]{"Tinggi", "Sedang", "Rendah"});
        cmbStatus = new JComboBox<>(new String[]{"Belum", "Selesai"});

        spinnerDeadline = new JSpinner(new SpinnerDateModel());
        spinnerDeadline.setEditor(new JSpinner.DateEditor(spinnerDeadline, "yyyy-MM-dd"));

        btnSimpan = new JButton("💾 Simpan");
        btnUpdate = new JButton("✏️ Update");
        btnHapus = new JButton("🗑️ Hapus");

        panelInput.add(new JLabel("Judul:")); panelInput.add(txtJudul);
        panelInput.add(new JLabel("Deskripsi:")); panelInput.add(new JScrollPane(txtDeskripsi));
        panelInput.add(new JLabel("Kategori:")); panelInput.add(cmbKategori);
        panelInput.add(new JLabel("Prioritas:")); panelInput.add(cmbPrioritas);
        panelInput.add(new JLabel("Status:")); panelInput.add(cmbStatus);
        panelInput.add(new JLabel("Deadline:")); panelInput.add(spinnerDeadline);
        panelInput.add(btnSimpan); panelInput.add(btnUpdate);

        JPanel panelTengah = new JPanel(new BorderLayout());
        panelTengah.setBackground(pinkSoft);
        tabelModel = new DefaultTableModel(new String[]{"ID", "Judul", "Deskripsi", "Kategori", "Prioritas", "Status", "Deadline"}, 0);
        tabelTugas = new JTable(tabelModel);
        JScrollPane scrollPane = new JScrollPane(tabelTugas);
        panelTengah.add(scrollPane, BorderLayout.CENTER);
        panelTengah.add(btnHapus, BorderLayout.SOUTH);

        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtCari = new JTextField(15);
        btnCari = new JButton("🔍 Cari");
        panelCari.setBackground(pinkSoft);
        panelCari.add(new JLabel("Cari Judul:"));
        panelCari.add(txtCari);
        panelCari.add(btnCari);

        add(panelInput, BorderLayout.WEST);
        add(panelTengah, BorderLayout.CENTER);
        add(panelCari, BorderLayout.NORTH);

        tampilkanTugas();

        btnSimpan.addActionListener(e -> simpanTugas());
        btnUpdate.addActionListener(e -> updateTugas());
        btnHapus.addActionListener(e -> hapusTugas());
        btnCari.addActionListener(e -> cariTugas());

        tabelTugas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int baris = tabelTugas.getSelectedRow();
                if (baris != -1) {
                    selectedId = (int) tabelModel.getValueAt(baris, 0);
                    txtJudul.setText((String) tabelModel.getValueAt(baris, 1));
                    txtDeskripsi.setText((String) tabelModel.getValueAt(baris, 2));
                    cmbKategori.setSelectedItem(tabelModel.getValueAt(baris, 3));
                    cmbPrioritas.setSelectedItem(tabelModel.getValueAt(baris, 4));
                    cmbStatus.setSelectedItem(tabelModel.getValueAt(baris, 5));
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse((String) tabelModel.getValueAt(baris, 6));
                        spinnerDeadline.setValue(date);
                    } catch (Exception ex) {
                        spinnerDeadline.setValue(new Date());
                    }
                }
            }
        });
    }

    private void simpanTugas() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO tugas (judul, deskripsi, kategori, prioritas, status, deadline) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtJudul.getText());
            pst.setString(2, txtDeskripsi.getText());
            pst.setString(3, (String) cmbKategori.getSelectedItem());
            pst.setString(4, (String) cmbPrioritas.getSelectedItem());
            pst.setString(5, (String) cmbStatus.getSelectedItem());
            pst.setDate(6, new java.sql.Date(((Date) spinnerDeadline.getValue()).getTime()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Tugas disimpan!");
            tampilkanTugas();
            resetForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal simpan: " + ex.getMessage());
        }
    }

    private void updateTugas() {
        if (selectedId == -1) return;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE tugas SET judul=?, deskripsi=?, kategori=?, prioritas=?, status=?, deadline=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtJudul.getText());
            pst.setString(2, txtDeskripsi.getText());
            pst.setString(3, (String) cmbKategori.getSelectedItem());
            pst.setString(4, (String) cmbPrioritas.getSelectedItem());
            pst.setString(5, (String) cmbStatus.getSelectedItem());
            pst.setDate(6, new java.sql.Date(((Date) spinnerDeadline.getValue()).getTime()));
            pst.setInt(7, selectedId);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Tugas diperbarui!");
            tampilkanTugas();
            resetForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal update: " + ex.getMessage());
        }
    }

    private void hapusTugas() {
        int row = tabelTugas.getSelectedRow();
        if (row == -1) return;
        int id = (int) tabelModel.getValueAt(row, 0);
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("DELETE FROM tugas WHERE id = ?");
            pst.setInt(1, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Tugas dihapus!");
            tampilkanTugas();
            resetForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal hapus: " + ex.getMessage());
        }
    }

    private void cariTugas() {
        tabelModel.setRowCount(0);
        try {
            Connection conn = DatabaseConnection.getConnection();
            String keyword = txtCari.getText();
            String sql = "SELECT * FROM tugas WHERE judul LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                tabelModel.addRow(new Object[]{
                        rs.getInt("id"), rs.getString("judul"), rs.getString("deskripsi"),
                        rs.getString("kategori"), rs.getString("prioritas"),
                        rs.getString("status"), rs.getString("deadline")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal cari: " + ex.getMessage());
        }
    }

    private void tampilkanTugas() {
        tabelModel.setRowCount(0);
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM tugas");
            while (rs.next()) {
                tabelModel.addRow(new Object[]{
                        rs.getInt("id"), rs.getString("judul"), rs.getString("deskripsi"),
                        rs.getString("kategori"), rs.getString("prioritas"),
                        rs.getString("status"), rs.getString("deadline")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal tampilkan: " + ex.getMessage());
        }
    }

    private void resetForm() {
        txtJudul.setText("");
        txtDeskripsi.setText("");
        cmbKategori.setSelectedIndex(0);
        cmbPrioritas.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        spinnerDeadline.setValue(new Date());
        selectedId = -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}