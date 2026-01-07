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

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(34, 139, 34));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblBrand = new JLabel("E-Waste Admin Panel");
        lblBrand.setFont(new Font("Arial", Font.BOLD, 22));
        lblBrand.setForeground(Color.WHITE);
        headerPanel.add(lblBrand, BorderLayout.WEST);

        JButton btnHeaderLogout = new JButton("Logout");
        btnHeaderLogout.setBackground(new Color(220, 53, 69));
        btnHeaderLogout.setForeground(Color.WHITE);
        btnHeaderLogout.setFocusPainted(false);
        btnHeaderLogout.setFont(new Font("Arial", Font.BOLD, 12));
        btnHeaderLogout.addActionListener(e -> {
            AuthController.logout();
            new LoginView().setVisible(true);
            this.dispose();
        });
        headerPanel.add(btnHeaderLogout, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(10, 1, 5, 5));
        sidebar.setBackground(new Color(245, 245, 245));
        sidebar.setPreferredSize(new Dimension(230, getHeight()));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        JLabel menuTitle = new JLabel("MENU ADMIN", SwingConstants.CENTER);
        menuTitle.setFont(new Font("Arial", Font.BOLD, 12));
        menuTitle.setForeground(Color.GRAY);
        menuTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        sidebar.add(menuTitle);

        JButton btnDash = createSidebarButton("Dashboard");
        JButton btnKategori = createSidebarButton("Manajemen Kategori");
        JButton btnLokasi = createSidebarButton("Manajemen Lokasi");
        JButton btnUser = createSidebarButton("Manajemen User");
        JButton btnRiwayat = createSidebarButton("Riwayat Transaksi");

        sidebar.add(btnDash);
        sidebar.add(btnKategori);
        sidebar.add(btnLokasi);
        sidebar.add(btnUser);
        sidebar.add(btnRiwayat);

        add(sidebar, BorderLayout.WEST);

        // Content Area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Dashboard Home Panel
        contentPanel.add(new AdminHomePanel(), "Dashboard");

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
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton("  " + text);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(34, 139, 34)),
                BorderFactory.createEmptyBorder(10, 15, 10, 10)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // home.
    class AdminHomePanel extends JPanel {
        public AdminHomePanel() {
            setLayout(new BorderLayout(15, 15));
            setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            setBackground(Color.WHITE);

            // Title
            JLabel lblTitle = new JLabel("Dashboard Admin");
            lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
            lblTitle.setForeground(new Color(34, 139, 34));
            lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
            add(lblTitle, BorderLayout.NORTH);

            // Main container
            JPanel mainContainer = new JPanel();
            mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
            mainContainer.setBackground(Color.WHITE);

            // Stats
            JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
            statsPanel.setBackground(Color.WHITE);
            statsPanel.setMaximumSize(new Dimension(2000, 120));
            statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            statsPanel.add(createStatCard("Total Transaksi",
                    String.valueOf(transaksiController.getTotalTransaksi()),
                    new Color(100, 149, 237)));
            statsPanel.add(createStatCard("Total Berat (Kg)",
                    String.valueOf(transaksiController.getTotalBerat()),
                    new Color(60, 179, 113)));
            statsPanel.add(createStatCard("User Aktif",
                    String.valueOf(transaksiController.getUserAktif()),
                    new Color(255, 165, 0)));
            statsPanel.add(createStatCard("Lokasi Aktif",
                    String.valueOf(transaksiController.getLokasiAktif()),
                    new Color(220, 53, 69)));

            mainContainer.add(statsPanel);
            mainContainer.add(Box.createRigidArea(new Dimension(0, 25)));

            // History Transaksi Baru
            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.setBackground(Color.WHITE);
            tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblTableTitle = new JLabel("10 Transaksi Terbaru");
            lblTableTitle.setFont(new Font("Arial", Font.BOLD, 18));
            lblTableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            tablePanel.add(lblTableTitle, BorderLayout.NORTH);

            JTable recentTable = createRecentTable();
            recentTable.setFont(new Font("Arial", Font.PLAIN, 13));
            recentTable.setRowHeight(28);
            recentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
            recentTable.getTableHeader().setBackground(new Color(240, 240, 240));

            JScrollPane scrollPane = new JScrollPane(recentTable);
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            tablePanel.add(scrollPane, BorderLayout.CENTER);

            mainContainer.add(tablePanel);

            add(mainContainer, BorderLayout.CENTER);
        }

        private JPanel createStatCard(String title, String value, Color accentColor) {
            JPanel card = new JPanel(new BorderLayout(5, 5));
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 5, 0, 0, accentColor),
                            BorderFactory.createEmptyBorder(15, 15, 15, 15))));
            card.setBackground(Color.WHITE);

            JLabel lblTitle = new JLabel(title);
            lblTitle.setFont(new Font("Arial", Font.PLAIN, 13));
            lblTitle.setForeground(Color.GRAY);

            JLabel lblValue = new JLabel(value);
            lblValue.setFont(new Font("Arial", Font.BOLD, 32));
            lblValue.setForeground(accentColor);

            card.add(lblTitle, BorderLayout.NORTH);
            card.add(lblValue, BorderLayout.CENTER);
            return card;
        }

        private JTable createRecentTable() {
            String[] columns = { "No", "User", "Kategori", "Lokasi", "Berat", "Total", "Status", "Tanggal" };
            // Fetch data
            java.util.List<com.ewaste.models.Transaksi> list = transaksiController.getRecentTransaksi();
            String[][] data = new String[list.size()][8];
            for (int i = 0; i < list.size(); i++) {
                com.ewaste.models.Transaksi t = list.get(i);
                data[i][0] = String.valueOf(i + 1);
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