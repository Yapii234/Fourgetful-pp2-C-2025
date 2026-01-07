package com.ewaste.views;

import com.ewaste.controllers.AuthController;
import com.ewaste.models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthController authController;

    public LoginView() {
        authController = new AuthController();
        initComponents();
    }

    private void initComponents() {
        setTitle("Login - E-Waste Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 139, 34));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        JLabel titleLabel = new JLabel("E-Waste Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(lblUsername, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(250, 32));
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(250, 32));
        formPanel.add(passwordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(Color.WHITE);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(34, 139, 34));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(140, 38));
        loginButton.setFocusPainted(false);

        JButton registerButton = new JButton("Register (User)");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        registerButton.setPreferredSize(new Dimension(140, 38));
        registerButton.setFocusPainted(false);

        loginButton.addActionListener(this::handleLogin);
        registerButton.addActionListener(e -> handleRegister());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Password required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = authController.login(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + user.getNama());
            this.dispose();

            if ("admin".equalsIgnoreCase(user.getRole())) {
                new AdminDashboardView().setVisible(true);
            } else {
                new UserDashboardView().setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        new RegisterView().setVisible(true);
        this.dispose();
    }
}
