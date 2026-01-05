package com.ewaste.views;

import com.ewaste.controllers.AuthController;
import com.ewaste.controllers.KategoriController;
import com.ewaste.controllers.LokasiController;
import com.ewaste.controllers.TransaksiController;
import com.ewaste.models.Kategori;
import com.ewaste.models.Lokasi;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserTransaksiView extends JPanel {
    private JComboBox<Kategori> cmbKategori;
    private JComboBox<Lokasi> cmbLokasi;
    private JTextField txtBerat;
    private JTextArea txtCatatan;
    private JLabel lblEstimasi;
    private TransaksiController transController;
    private KategoriController katController;
    private LokasiController lokController;

    public UserTransaksiView() {
        transController = new TransaksiController();
        katController = new KategoriController();
        lokController = new LokasiController();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        formPanel.add(new JLabel("Pilih Kategori E-Waste:"));
        cmbKategori = new JComboBox<>();
        loadKategori();
        formPanel.add(cmbKategori);

        formPanel.add(new JLabel("Perkiraan Berat (Kg):"));
        txtBerat = new JTextField("0");
        formPanel.add(txtBerat);

        formPanel.add(new JLabel("Pilih Lokasi Penjemputan:"));
        cmbLokasi = new JComboBox<>();
        loadLokasi();
        formPanel.add(cmbLokasi);

        formPanel.add(new JLabel("Catatan Tambahan:"));
        txtCatatan = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(txtCatatan));

        formPanel.add(new JLabel("Estimasi Total Harga (Rp):"));
        lblEstimasi = new JLabel("0");
        lblEstimasi.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblEstimasi);

        JButton btnHitung = new JButton("Hitung Estimasi");
        formPanel.add(btnHitung);

        JButton btnSubmit = new JButton("Kirim Permintaan");
        btnSubmit.setBackground(new Color(34, 139, 34));
        btnSubmit.setForeground(Color.WHITE);
        formPanel.add(btnSubmit);

        add(new JLabel("Buat Transaksi E-Waste Baru", SwingConstants.CENTER), BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);

        // Listeners
        btnHitung.addActionListener(e -> calculateTotal());
        btnSubmit.addActionListener(e -> submitTransaksi());
    }

    private void loadKategori() {
        List<Kategori> list = katController.getAllKategori();
        for (Kategori k : list) {
            cmbKategori.addItem(k);
        }
    }

    private void loadLokasi() {
        List<Lokasi> list = lokController.getAllLokasi();
        for (Lokasi l : list) {
            cmbLokasi.addItem(l);
        }
    }

    private void calculateTotal() {
        try {
            double berat = Double.parseDouble(txtBerat.getText());
            if (berat < 0.1) {
                JOptionPane.showMessageDialog(this, "Berat minimal 0.1 Kg");
                return;
            }
            Kategori k = (Kategori) cmbKategori.getSelectedItem();
            if (k != null) {
                double total = berat * k.getHargaPerKg();
                lblEstimasi.setText(String.format("Rp %.2f", total));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Masukkan berat yang valid (angka)");
        }
    }

    private void submitTransaksi() {
        try {
            double berat = Double.parseDouble(txtBerat.getText());
            if (berat < 0.1) {
                JOptionPane.showMessageDialog(this, "Berat minimal 0.1 Kg");
                return;
            }
            Kategori k = (Kategori) cmbKategori.getSelectedItem();
            Lokasi l = (Lokasi) cmbLokasi.getSelectedItem();

            if (k == null || l == null) {
                JOptionPane.showMessageDialog(this, "Pilih kategori dan lokasi");
                return;
            }

            calculateTotal(); // Ensure calculation update

            int userId = AuthController.getCurrentUser().getId();

            if (transController.createTransaksi(userId, k.getId(), l.getId(), berat, txtCatatan.getText())) {
                JOptionPane.showMessageDialog(this, "Transaksi Berhasil Dibuat! Silahkan tunggu konfirmasi admin.");
                // Reset form
                txtBerat.setText("0");
                txtCatatan.setText("");
                lblEstimasi.setText("0");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal membuat transaksi.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input tidak valid");
        }
    }
}
