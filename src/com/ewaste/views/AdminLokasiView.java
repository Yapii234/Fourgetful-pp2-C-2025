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

    public AdminLokasiView() {
        controller = new LokasiController();
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Lokasi"));

        formPanel.add(new JLabel("Nama Lokasi:"));
        txtNama = new JTextField();
        formPanel.add(txtNama);

        formPanel.add(new JLabel("Alamat:"));
        txtAlamat = new JTextField();
        formPanel.add(txtAlamat);

        formPanel.add(new JLabel("Kota:"));
        txtKota = new JTextField();
        formPanel.add(txtKota);

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

        String[] columns = { "ID", "Nama", "Alamat", "Kota" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

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
        List<Lokasi> list = controller.getAllLokasi();
        for (Lokasi l : list) {
            tableModel.addRow(new Object[] { l.getId(), l.getNamaLokasi(), l.getAlamat(), l.getKota() });
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
        int id = (int) tableModel.getValueAt(row, 0);
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
            int id = (int) tableModel.getValueAt(row, 0);
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
