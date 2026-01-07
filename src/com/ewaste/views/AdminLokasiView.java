package com.ewaste.views;

import com.ewaste.controllers.LokasiController;
import com.ewaste.models.Lokasi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminLokasiView extends JPanel {
    private LokasiController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNama, txtAlamat, txtKota;
    private List<Lokasi> lokasiList;

    public AdminLokasiView() {
        controller = new LokasiController();
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("Manajemen Lokasi Penjemputan");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(34, 139, 34));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Lokasi"));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("Nama Lokasi:"));
        txtNama = new JTextField();
        formPanel.add(txtNama);

        formPanel.add(new JLabel("Alamat:"));
        txtAlamat = new JTextField();
        formPanel.add(txtAlamat);

        formPanel.add(new JLabel("Kota:"));
        txtKota = new JTextField();
        formPanel.add(txtKota);

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

        String[] columns = { "No", "Nama", "Alamat", "Kota" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Data Lokasi"));
        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addLokasi());
        btnEdit.addActionListener(e -> editLokasi());
        btnDelete.addActionListener(e -> deleteLokasi());
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
        lokasiList = controller.getAllLokasi();

        int no = 1;
        for (Lokasi l : lokasiList) {
            tableModel.addRow(new Object[] { no++, l.getNamaLokasi(), l.getAlamat(), l.getKota() });
        }
    }

    private void loadSelection() {
        int row = table.getSelectedRow();
        txtNama.setText(tableModel.getValueAt(row, 1).toString());
        txtAlamat.setText(tableModel.getValueAt(row, 2).toString());
        txtKota.setText(tableModel.getValueAt(row, 3).toString());
    }

    private void clearForm() {
        txtNama.setText("");
        txtAlamat.setText("");
        txtKota.setText("");
        table.clearSelection();
    }

    private void addLokasi() {
        if (controller.addLokasi(txtNama.getText(), txtAlamat.getText(), txtKota.getText())) {
            JOptionPane.showMessageDialog(this, "Berhasil");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal");
        }
    }

    private void editLokasi() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = lokasiList.get(row).getId();
        if (controller.updateLokasi(id, txtNama.getText(), txtAlamat.getText(), txtKota.getText())) {
            JOptionPane.showMessageDialog(this, "Berhasil");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal");
        }
    }

    private void deleteLokasi() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        if (JOptionPane.showConfirmDialog(this, "Yakin?") == JOptionPane.YES_OPTION) {
            int id = lokasiList.get(row).getId();
            if (controller.deleteLokasi(id)) {
                JOptionPane.showMessageDialog(this, "Berhasil");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal");
            }
        }
    }
}
