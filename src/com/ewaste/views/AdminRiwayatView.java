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
    private List<Transaksi> transaksiList;

    public AdminRiwayatView() {
        controller = new TransaksiController();
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setBackground(Color.WHITE);

        // Top Panel with Title and Buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Riwayat Transaksi");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(34, 139, 34));
        topPanel.add(lblTitle, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Arial", Font.PLAIN, 13));
        btnRefresh.setPreferredSize(new Dimension(100, 32));
        btnRefresh.setFocusPainted(false);

        JButton btnConfirm = new JButton("Konfirmasi (Selesai)");
        btnConfirm.setFont(new Font("Arial", Font.PLAIN, 13));
        btnConfirm.setBackground(new Color(34, 139, 34));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setPreferredSize(new Dimension(160, 32));
        btnConfirm.setFocusPainted(false);

        JButton btnReject = new JButton("Tolak / Batalkan");
        btnReject.setFont(new Font("Arial", Font.PLAIN, 13));
        btnReject.setBackground(new Color(220, 53, 69));
        btnReject.setForeground(Color.WHITE);
        btnReject.setPreferredSize(new Dimension(140, 32));
        btnReject.setFocusPainted(false);

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnConfirm);
        buttonPanel.add(btnReject);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "No", "User", "Kategori", "Lokasi", "Berat (Kg)", "Total (Rp)", "Status", "Tanggal" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setForeground(Color.DARK_GRAY);
        table.setSelectionBackground(new Color(220, 240, 220));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        add(scrollPane, BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadData());
        btnConfirm.addActionListener(e -> updateStatus("selesai"));
        btnReject.addActionListener(e -> updateStatus("dibatalkan"));

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        transaksiList = controller.getAllTransaksi();

        int no = 1;
        for (Transaksi t : transaksiList) {
            tableModel.addRow(new Object[] {
                    no++, t.getNamaUser(), t.getNamaKategori(), t.getNamaLokasi(),
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

        int id = transaksiList.get(row).getId();
        if (controller.updateStatus(id, status)) {
            JOptionPane.showMessageDialog(this, "Status diubah menjadi: " + status);
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengubah status");
        }
    }
}