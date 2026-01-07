package com.ewaste.views;

import com.ewaste.controllers.AuthController;
import javax.swing.*;
import java.awt.*;

public class RegisterView extends JFrame {
    private JTextField txtNama, txtEmail, txtTelepon, txtUsername;
    private JPasswordField txtPassword;
    private AuthController authController;

    public RegisterView() {
        authController = new AuthController();
        initComponents();
    }

    private void initComponents() {
        setTitle("Register - E-Waste Management System");
        setSize(550, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 139, 34));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        JLabel titleLabel = new JLabel("Buat Akun Baru");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addFormField(formPanel, gbc, "Nama Lengkap:", txtNama = new JTextField(20), row++);
        addFormField(formPanel, gbc, "Email:", txtEmail = new JTextField(20), row++);
        addFormField(formPanel, gbc, "Telepon:", txtTelepon = new JTextField(20), row++);
        addFormField(formPanel, gbc, "Username:", txtUsername = new JTextField(20), row++);
        addFormField(formPanel, gbc, "Password:", txtPassword = new JPasswordField(20), row++);

        add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnRegister = new JButton("Daftar Sekarang");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setBackground(new Color(34, 139, 34));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setPreferredSize(new Dimension(160, 38));
        btnRegister.setFocusPainted(false);

        JButton btnLogin = new JButton("Sudah punya akun? Login");
        btnLogin.setFont(new Font("Arial", Font.PLAIN, 14));
        btnLogin.setPreferredSize(new Dimension(200, 38));
        btnLogin.setFocusPainted(false);

        btnRegister.addActionListener(e -> handleRegister());
        btnLogin.addActionListener(e -> {
            new LoginView().setVisible(true);
            this.dispose();
        });

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnLogin);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, Component comp, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.CENTER;

        if (comp instanceof JTextField) {
            ((JTextField) comp).setFont(new Font("Arial", Font.PLAIN, 14));
            comp.setPreferredSize(new Dimension(280, 32));
        } else if (comp instanceof JPasswordField) {
            ((JPasswordField) comp).setFont(new Font("Arial", Font.PLAIN, 14));
            comp.setPreferredSize(new Dimension(280, 32));
        }

        panel.add(comp, gbc);
    }

    private void handleRegister() {
        String nama = txtNama.getText();
        String email = txtEmail.getText();
        String telepon = txtTelepon.getText();
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (nama.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua data wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (authController.register(nama, email, telepon, username, password)) {
            JOptionPane.showMessageDialog(this, "Registrasi Berhasil! Silahkan Login.");
            new LoginView().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registrasi Gagal! Email/Username mungkin sudah dipakai.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
