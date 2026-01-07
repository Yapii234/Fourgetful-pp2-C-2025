package com.ewaste.views;

import com.ewaste.controllers.AuthController;
import com.ewaste.controllers.TransaksiController;
import com.ewaste.models.Transaksi;
import com.ewaste.models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class UserDashboardView extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private TransaksiController transaksiController;
    private User currentUser;

    public UserDashboardView() {
        transaksiController = new TransaksiController();
        currentUser = AuthController.getCurrentUser();
        initComponents();
    }

    private void initComponents() {
        setTitle("User Dashboard - E-Waste Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(34, 139, 34)); 
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblBrand = new JLabel("E-Waste System");
        lblBrand.setFont(new Font("Arial", Font.BOLD, 22));
        lblBrand.setForeground(Color.WHITE);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);

        JLabel lblUser = new JLabel(currentUser.getNama() + "  |  ");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(220, 53, 69)); 
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogout.addActionListener(e -> {
            AuthController.logout();
            new LoginView().setVisible(true);
            this.dispose();
        });

        userPanel.add(lblUser);
        userPanel.add(btnLogout);

        headerPanel.add(lblBrand, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        //Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(10, 1, 5, 5));
        sidebar.setBackground(new Color(245, 245, 245));
        sidebar.setPreferredSize(new Dimension(230, getHeight()));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        JLabel menuTitle = new JLabel("MENU UTAMA", SwingConstants.CENTER);
        menuTitle.setFont(new Font("Arial", Font.BOLD, 12));
        menuTitle.setForeground(Color.GRAY);
        menuTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        sidebar.add(menuTitle);

        JButton btnDash = createSidebarButton("Dashboard");
        JButton btnTrans = createSidebarButton("Transaksi E-Waste");
        JButton btnRiwayat = createSidebarButton("Riwayat Transaksi");

        sidebar.add(btnDash);
        sidebar.add(btnTrans);
        sidebar.add(btnRiwayat);

        add(sidebar, BorderLayout.WEST);

        //Content Panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new UserHomePanel(), "Dashboard");
        contentPanel.add(new UserTransaksiView(), "Transaksi");
        contentPanel.add(new UserRiwayatView(), "Riwayat");

        add(contentPanel, BorderLayout.CENTER);

        // Actions
        btnDash.addActionListener(e -> {
            contentPanel.add(new UserHomePanel(), "Dashboard");
            cardLayout.show(contentPanel, "Dashboard");
        });
        btnTrans.addActionListener(e -> cardLayout.show(contentPanel, "Transaksi"));
        btnRiwayat.addActionListener(e -> {
            contentPanel.add(new UserRiwayatView(), "Riwayat");
            cardLayout.show(contentPanel, "Riwayat");
        });
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

    class UserHomePanel extends JPanel {
        public UserHomePanel() {
            setLayout(new BorderLayout(20, 20));
            setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            setBackground(Color.WHITE);

            // Welcome Message
            JLabel lblWelcome = new JLabel("SELAMAT DATANG, " + currentUser.getNama().toUpperCase() + "!");
            lblWelcome.setFont(new Font("Arial", Font.BOLD, 28));
            lblWelcome.setForeground(new Color(34, 139, 34));
            add(lblWelcome, BorderLayout.NORTH);

            JPanel centerContainer = new JPanel();
            centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
            centerContainer.setOpaque(false);

            // Stats Area
            // Calculate Stats
            List<Transaksi> history = transaksiController.getUserTransaksi(currentUser.getId());
            double totalBerat = history.stream().mapToDouble(Transaksi::getBerat).sum();

            int poin = currentUser.getSaldoPoin();
            long rupiahValue = poin * 100L;

            JPanel statsGrid = new JPanel(new GridLayout(1, 3, 20, 0));
            statsGrid.setOpaque(false);
            statsGrid.setMaximumSize(new Dimension(2000, 140));
            statsGrid.setPreferredSize(new Dimension(800, 140));
            statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

            statsGrid.add(createStatCard("Saldo Poin", NumberFormat.getNumberInstance().format(poin) + " pts",
                    "Rp " + NumberFormat.getNumberInstance().format(rupiahValue), new Color(255, 165, 0)));
            statsGrid.add(createStatCard("Total Transaksi", history.size() + " Transaksi", "Selama bergabung",
                    new Color(100, 149, 237)));
            statsGrid.add(createStatCard("Total Berat", String.format("%.1f kg", totalBerat), "Kontribusi",
                    new Color(60, 179, 113)));

            centerContainer.add(statsGrid);
            centerContainer.add(Box.createRigidArea(new Dimension(0, 30))); // Spacer

            //History terbaru Table
            JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
            tablePanel.setOpaque(false);
            tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblTableTitle = new JLabel("Riwayat Transaksi Terbaru");
            lblTableTitle.setFont(new Font("Arial", Font.BOLD, 18));
            tablePanel.add(lblTableTitle, BorderLayout.NORTH);

            String[] columns = { "Tanggal", "Kategori", "Lokasi", "Berat (Kg)", "Status" };
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            int count = 0;
            for (Transaksi t : history) {
                if (count++ >= 5)
                    break;
                model.addRow(new Object[] { t.getTanggalTransaksi(), t.getNamaKategori(), t.getNamaLokasi(),
                        t.getBerat(), t.getStatus() });
            }

            JTable table = new JTable(model);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
            table.getTableHeader().setBackground(new Color(240, 240, 240));

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(800, 200));
            tablePanel.add(scrollPane, BorderLayout.CENTER);

            centerContainer.add(tablePanel);
            centerContainer.add(Box.createRigidArea(new Dimension(0, 20)));

            // D. Action Button
            JButton btnNewTrans = new JButton("BUAT TRANSAKSI BARU");
            btnNewTrans.setFont(new Font("Arial", Font.BOLD, 14));
            btnNewTrans.setBackground(new Color(34, 139, 34)); 
            btnNewTrans.setForeground(Color.WHITE);
            btnNewTrans.setFocusPainted(false);
            btnNewTrans.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnNewTrans.setMaximumSize(new Dimension(250, 45));
            btnNewTrans.addActionListener(e -> cardLayout.show(contentPanel, "Transaksi"));

            centerContainer.add(btnNewTrans);

            add(centerContainer, BorderLayout.CENTER);
        }

        private JPanel createStatCard(String title, String mainValue, String subValue, Color accentColor) {
            JPanel card = new JPanel(new BorderLayout(5, 5));
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 5, 0, 0, accentColor),
                            BorderFactory.createEmptyBorder(15, 15, 15, 15))));
            card.setBackground(Color.WHITE);

            JLabel lblTitle = new JLabel(title);
            lblTitle.setFont(new Font("Arial", Font.PLAIN, 14));
            lblTitle.setForeground(Color.GRAY);

            JLabel lblMain = new JLabel(mainValue);
            lblMain.setFont(new Font("Arial", Font.BOLD, 26));

            JLabel lblSub = new JLabel(subValue);
            lblSub.setFont(new Font("Arial", Font.PLAIN, 13));
            lblSub.setForeground(new Color(100, 100, 100));

            card.add(lblTitle, BorderLayout.NORTH);
            card.add(lblMain, BorderLayout.CENTER);
            card.add(lblSub, BorderLayout.SOUTH);
            return card;
        }
    }
}
