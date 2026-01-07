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

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setBackground(Color.WHITE);

        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("Buat Transaksi E-Waste Baru");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(34, 139, 34));
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1.0;

        int row = 0;

        // Kategori
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lblKat = new JLabel("Pilih Kategori E-Waste:");
        lblKat.setFont(new Font("Arial", Font.PLAIN, 14));
        formContainer.add(lblKat, gbc);

        gbc.gridx = 1;
        gbc.gridy = row++;
        cmbKategori = new JComboBox<>();
        cmbKategori.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbKategori.setPreferredSize(new Dimension(300, 30));
        loadKategori();
        formContainer.add(cmbKategori, gbc);

        // Berat
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lblBerat = new JLabel("Perkiraan Berat (Kg):");
        lblBerat.setFont(new Font("Arial", Font.PLAIN, 14));
        formContainer.add(lblBerat, gbc);

        gbc.gridx = 1;
        gbc.gridy = row++;
        txtBerat = new JTextField("0");
        txtBerat.setFont(new Font("Arial", Font.PLAIN, 14));
        txtBerat.setPreferredSize(new Dimension(300, 30));
        formContainer.add(txtBerat, gbc);

        // Lokasi
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lblLok = new JLabel("Pilih Lokasi Penjemputan:");
        lblLok.setFont(new Font("Arial", Font.PLAIN, 14));
        formContainer.add(lblLok, gbc);

        gbc.gridx = 1;
        gbc.gridy = row++;
        cmbLokasi = new JComboBox<>();
        cmbLokasi.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbLokasi.setPreferredSize(new Dimension(300, 30));
        loadLokasi();
        formContainer.add(cmbLokasi, gbc);

        // Catatan
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblCat = new JLabel("Catatan Tambahan:");
        lblCat.setFont(new Font("Arial", Font.PLAIN, 14));
        formContainer.add(lblCat, gbc);

        gbc.gridx = 1;
        gbc.gridy = row++;
        gbc.anchor = GridBagConstraints.CENTER;
        txtCatatan = new JTextArea(3, 25);
        txtCatatan.setFont(new Font("Arial", Font.PLAIN, 14));
        txtCatatan.setLineWrap(true);
        txtCatatan.setWrapStyleWord(true);
        txtCatatan.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        formContainer.add(txtCatatan, gbc);

        // Estimasi
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lblEstLabel = new JLabel("Estimasi Total Harga:");
        lblEstLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formContainer.add(lblEstLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = row++;
        lblEstimasi = new JLabel("Rp 0");
        lblEstimasi.setFont(new Font("Arial", Font.BOLD, 18));
        lblEstimasi.setForeground(new Color(34, 139, 34));
        formContainer.add(lblEstimasi, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnHitung = new JButton("Hitung Estimasi");
        btnHitung.setFont(new Font("Arial", Font.PLAIN, 14));
        btnHitung.setPreferredSize(new Dimension(150, 35));
        btnHitung.setFocusPainted(false);
        buttonPanel.add(btnHitung);

        JButton btnSubmit = new JButton("Kirim Permintaan");
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 14));
        btnSubmit.setBackground(new Color(34, 139, 34));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setPreferredSize(new Dimension(180, 35));
        btnSubmit.setFocusPainted(false);
        buttonPanel.add(btnSubmit);

        formContainer.add(buttonPanel, gbc);

        add(formContainer, BorderLayout.CENTER);

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
