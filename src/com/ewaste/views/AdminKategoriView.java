package com.ewaste.views;

import com.ewaste.controllers.KategoriController;
import com.ewaste.models.Kategori;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminKategoriView extends JPanel {
    private KategoriController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNama, txtDeskripsi, txtHarga;
    private List<Kategori> kategoriList;

    public AdminKategoriView() {
        controller = new KategoriController();
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("Manajemen Kategori E-Waste");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(34, 139, 34));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Kategori"));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("Nama Kategori:"));
        txtNama = new JTextField();
        formPanel.add(txtNama);

        formPanel.add(new JLabel("Deskripsi:"));
        txtDeskripsi = new JTextField();
        formPanel.add(txtDeskripsi);

        formPanel.add(new JLabel("Harga per KG:"));
        txtHarga = new JTextField();
        formPanel.add(txtHarga);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);

        JButton btnAdd = new JButton("Tambah");
        btnAdd.setBackground(new Color(34, 139, 34));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);

        JButton btnEdit = new JButton("Edit");
        btnEdit.setFocusPainted(false);

        JButton btnDelete = new JButton("Hapus");
        btnDelete.setBackground(new Color(220, 53, 69));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);

        JButton btnClear = new JButton("Clear");
        btnClear.setFocusPainted(false);

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "No", "Nama", "Deskripsi", "Harga" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Data Kategori"));
        add(scrollPane, BorderLayout.CENTER);

        // Listeners
        btnAdd.addActionListener(e -> addKategori());
        btnEdit.addActionListener(e -> editKategori());
        btnDelete.addActionListener(e -> deleteKategori());
        btnClear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                loadSelection();
            }
        });

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        kategoriList = controller.getAllKategori();

        int no = 1;
        for (Kategori k : kategoriList) {
            tableModel.addRow(new Object[] { no++, k.getNamaKategori(), k.getDeskripsi(), k.getHargaPerKg() });
        }
    }

    private void loadSelection() {
        int row = table.getSelectedRow();
        txtNama.setText(tableModel.getValueAt(row, 1).toString());
        txtDeskripsi.setText(tableModel.getValueAt(row, 2).toString());
        txtHarga.setText(tableModel.getValueAt(row, 3).toString());
    }

    private void clearForm() {
        txtNama.setText("");
        txtDeskripsi.setText("");
        txtHarga.setText("");
        table.clearSelection();
    }

    private void addKategori() {
        String nama = txtNama.getText();
        String desc = txtDeskripsi.getText();
        try {
            double harga = Double.parseDouble(txtHarga.getText());
            if (controller.addKategori(nama, desc, harga)) {
                JOptionPane.showMessageDialog(this, "Berhasil menambah kategori");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambah kategori");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus angka valid");
        }
    }

    private void editKategori() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu");
            return;
        }
        int id = kategoriList.get(row).getId();
        String nama = txtNama.getText();
        String desc = txtDeskripsi.getText();
        try {
            double harga = Double.parseDouble(txtHarga.getText());
            if (controller.updateKategori(id, nama, desc, harga)) {
                JOptionPane.showMessageDialog(this, "Berhasil update kategori");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal update kategori");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus angka valid");
        }
    }

    private void deleteKategori() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = kategoriList.get(row).getId();
            if (controller.deleteKategori(id)) {
                JOptionPane.showMessageDialog(this, "Berhasil hapus");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal hapus (Mungkin ada transaksi terkait)");
            }
        }
    }
}
