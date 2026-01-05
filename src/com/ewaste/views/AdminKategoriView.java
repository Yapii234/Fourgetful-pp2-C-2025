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

    public AdminKategoriView() {
        controller = new KategoriController();
        setLayout(new BorderLayout(10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Kategori"));

        formPanel.add(new JLabel("Nama Kategori:"));
        txtNama = new JTextField();
        formPanel.add(txtNama);

        formPanel.add(new JLabel("Deskripsi:"));
        txtDeskripsi = new JTextField();
        formPanel.add(txtDeskripsi);

        formPanel.add(new JLabel("Harga per KG:"));
        txtHarga = new JTextField();
        formPanel.add(txtHarga);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Hapus");
        JButton btnClear = new JButton("Clear");

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "Nama", "Deskripsi", "Harga" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

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
        List<Kategori> list = controller.getAllKategori();
        for (Kategori k : list) {
            tableModel.addRow(new Object[] { k.getId(), k.getNamaKategori(), k.getDeskripsi(), k.getHargaPerKg() });
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
        int id = (int) tableModel.getValueAt(row, 0);
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
            int id = (int) tableModel.getValueAt(row, 0);
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
