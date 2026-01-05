package com.ewaste.views;

import com.ewaste.controllers.AuthController;
import com.ewaste.controllers.TransaksiController;
import javax.swing.*;
import java.awt.*;

public class AdminDashboardView extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private TransaksiController transaksiController;

    public AdminDashboardView() {
        transaksiController = new TransaksiController();
        initComponents();
    }

    private void initComponents() {
        setTitle("Admin Dashboard - E-Waste Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(6, 1, 5, 5));
        sidebar.setBackground(Color.DARK_GRAY);
        sidebar.setPreferredSize(new Dimension(200, getHeight()));

        JButton btnDash = createSidebarButton("Dashboard");
        JButton btnKategori = createSidebarButton("Manajemen Kategori");
        JButton btnLokasi = createSidebarButton("Manajemen Lokasi");
        JButton btnUser = createSidebarButton("Manajemen User");
        JButton btnRiwayat = createSidebarButton("Riwayat Transaksi");
        JButton btnLogout = createSidebarButton("Logout");

        sidebar.add(btnDash);
        sidebar.add(btnKategori);
        sidebar.add(btnLokasi);
        sidebar.add(btnUser);
        sidebar.add(btnRiwayat);
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // Content Area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Add Panels
        // Dashboard Home Panel
        contentPanel.add(new AdminHomePanel(), "Dashboard");
        // Lazy load other panels or create them now
        contentPanel.add(new AdminKategoriView(), "Kategori");
        contentPanel.add(new AdminLokasiView(), "Lokasi");
        contentPanel.add(new AdminUserView(), "User");
        contentPanel.add(new AdminRiwayatView(), "Riwayat");

        add(contentPanel, BorderLayout.CENTER);

        // Actions
        btnDash.addActionListener(e -> cardLayout.show(contentPanel, "Dashboard"));
        btnKategori.addActionListener(e -> cardLayout.show(contentPanel, "Kategori"));
        btnLokasi.addActionListener(e -> cardLayout.show(contentPanel, "Lokasi"));
        btnUser.addActionListener(e -> cardLayout.show(contentPanel, "User"));
        btnRiwayat.addActionListener(e -> cardLayout.show(contentPanel, "Riwayat"));
        btnLogout.addActionListener(e -> {
            AuthController.logout();
            new LoginView().setVisible(true);
            this.dispose();
        });
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        return btn;
    }

    // Using Inner Class for Home Panel to keep it simple, or separate?
    // Project structure implies AdminDashboardView might just be the container +
    // home.
    class AdminHomePanel extends JPanel {
        public AdminHomePanel() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Stats
            JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
            statsPanel.add(createStatCard("Total Transaksi", String.valueOf(transaksiController.getTotalTransaksi())));
            statsPanel.add(createStatCard("Total Berat (Kg)", String.valueOf(transaksiController.getTotalBerat())));
            statsPanel.add(createStatCard("User Aktif", String.valueOf(transaksiController.getUserAktif())));
            statsPanel.add(createStatCard("Lokasi Aktif", String.valueOf(transaksiController.getLokasiAktif())));

            add(statsPanel, BorderLayout.NORTH);

            // Recent Transactions Table
            JScrollPane scrollPane = new JScrollPane(createRecentTable());
            scrollPane.setBorder(BorderFactory.createTitledBorder("10 Transaksi Terbaru"));
            add(scrollPane, BorderLayout.CENTER);
        }

        private JPanel createStatCard(String title, String value) {
            JPanel p = new JPanel(new BorderLayout());
            p.setBackground(new Color(240, 240, 240));
            p.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
            JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
            lblValue.setFont(new Font("Arial", Font.BOLD, 24));
            p.add(lblTitle, BorderLayout.NORTH);
            p.add(lblValue, BorderLayout.CENTER);
            return p;
        }

        private JTable createRecentTable() {
            String[] columns = { "ID", "User", "Kategori", "Lokasi", "Berat", "Total", "Status", "Tanggal" };
            // Fetch data
            java.util.List<com.ewaste.models.Transaksi> list = transaksiController.getRecentTransaksi();
            String[][] data = new String[list.size()][8];
            for (int i = 0; i < list.size(); i++) {
                com.ewaste.models.Transaksi t = list.get(i);
                data[i][0] = String.valueOf(t.getId());
                data[i][1] = t.getNamaUser();
                data[i][2] = t.getNamaKategori();
                data[i][3] = t.getNamaLokasi();
                data[i][4] = String.valueOf(t.getBerat());
                data[i][5] = String.valueOf(t.getTotalHarga());
                data[i][6] = t.getStatus();
                data[i][7] = t.getTanggalTransaksi().toString();
            }
            return new JTable(data, columns);
        }
    }
}