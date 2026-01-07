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
    private List<Transaksi> transaksiList; // Store the list to access real IDs

    public UserRiwayatView() {
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

        JButton btnCancel = new JButton("Batalkan Transaksi");
        btnCancel.setFont(new Font("Arial", Font.PLAIN, 13));
        btnCancel.setBackground(new Color(220, 53, 69));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setPreferredSize(new Dimension(150, 32));
        btnCancel.setFocusPainted(false);

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnCancel);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "No", "Kategori", "Lokasi", "Berat (Kg)", "Total (Rp)", "Status", "Tanggal" };
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
        btnCancel.addActionListener(e -> cancelTransaksi());

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        int userId = AuthController.getCurrentUser().getId();
        transaksiList = controller.getUserTransaksi(userId);

        int no = 1;
        for (Transaksi t : transaksiList) {
            tableModel.addRow(new Object[] {
                    no++,
                    t.getNamaKategori(), t.getNamaLokasi(),
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
            int id = transaksiList.get(row).getId();
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
