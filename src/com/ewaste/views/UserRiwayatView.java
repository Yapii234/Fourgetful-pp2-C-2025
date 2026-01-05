package com.ewaste.views;

import com.ewaste.controllers.AuthController;
import com.ewaste.controllers.TransaksiController;
import com.ewaste.models.Transaksi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserRiwayatView extends JPanel {
    private TransaksiController controller;
    private JTable table;
    private DefaultTableModel tableModel;

    public UserRiwayatView() {
        controller = new TransaksiController();
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Refresh");
        JButton btnCancel = new JButton("Batalkan Transaksi");

        topPanel.add(btnRefresh);
        topPanel.add(btnCancel);

        add(topPanel, BorderLayout.NORTH);

        String[] columns = { "ID", "Kategori", "Lokasi", "Berat", "Total", "Status", "Tanggal" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadData());
        btnCancel.addActionListener(e -> cancelTransaksi());

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        int userId = AuthController.getCurrentUser().getId();
        List<Transaksi> list = controller.getUserTransaksi(userId);
        for (Transaksi t : list) {
            tableModel.addRow(new Object[] {
                    t.getId(), t.getNamaKategori(), t.getNamaLokasi(),
                    t.getBerat(), t.getTotalHarga(), t.getStatus(), t.getCreatedAt()
            });
        }
    }

    private void cancelTransaksi() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi dulu");
            return;
        }

        String currentStatus = tableModel.getValueAt(row, 5).toString();
        if (!"pending".equalsIgnoreCase(currentStatus)) {
            JOptionPane.showMessageDialog(this, "Hanya transaksi 'pending' yang bisa dibatalkan.");
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Yakin batalkan?") == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            int userId = AuthController.getCurrentUser().getId();

            if (controller.cancelTransaksi(id, userId)) {
                JOptionPane.showMessageDialog(this, "Transaksi dibatalkan");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal membatalkan");
            }
        }
    }
}
