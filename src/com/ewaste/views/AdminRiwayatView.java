package com.ewaste.views;

import com.ewaste.controllers.TransaksiController;
import com.ewaste.models.Transaksi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminRiwayatView extends JPanel {
    private TransaksiController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    public AdminRiwayatView() {
        controller = new TransaksiController();
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Refresh");
        JButton btnConfirm = new JButton("Konfirmasi (Selesai)");
        JButton btnReject = new JButton("Tolak / Batalkan");

        topPanel.add(btnRefresh);
        topPanel.add(btnConfirm);
        topPanel.add(btnReject);

        add(topPanel, BorderLayout.NORTH);

        String[] columns = { "ID", "User", "Kategori", "Lokasi", "Berat", "Total", "Status", "Tanggal" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadData());
        btnConfirm.addActionListener(e -> updateStatus("selesai"));
        btnReject.addActionListener(e -> updateStatus("dibatalkan"));

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Transaksi> list = controller.getAllTransaksi();
        for (Transaksi t : list) {
            tableModel.addRow(new Object[] {
                    t.getId(), t.getNamaUser(), t.getNamaKategori(), t.getNamaLokasi(),
                    t.getBerat(), t.getTotalHarga(), t.getStatus(), t.getCreatedAt()
            });
        }
    }

    private void updateStatus(String status) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi dulu");
            return;
        }

        String currentStatus = tableModel.getValueAt(row, 6).toString();
        if (!"pending".equalsIgnoreCase(currentStatus)) {
            JOptionPane.showMessageDialog(this, "Hanya transaksi 'pending' yang bisa diubah statusnya.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        if (controller.updateStatus(id, status)) {
            JOptionPane.showMessageDialog(this, "Status diubah menjadi: " + status);
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengubah status");
        }
    }
}