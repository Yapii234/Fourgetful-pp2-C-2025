package com.ewaste.views;

import com.ewaste.controllers.UserController;
import com.ewaste.models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminUserView extends JPanel {
    private UserController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNama, txtEmail, txtTelepon, txtUsername, txtPassword;
    private JComboBox<String> cmbRole;

    public AdminUserView() {
        controller = new UserController();
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form User"));

        formPanel.add(new JLabel("Nama:"));
        txtNama = new JTextField();
        formPanel.add(txtNama);

        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("Telepon:"));
        txtTelepon = new JTextField();
        formPanel.add(txtTelepon);

        formPanel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField(); // Should be JPasswordField, but for simple edit text is easier
        formPanel.add(txtPassword);

        formPanel.add(new JLabel("Role:"));
        cmbRole = new JComboBox<>(new String[] { "user", "admin" });
        formPanel.add(cmbRole);

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

        String[] columns = { "ID", "Nama", "Email", "Telepon", "Username", "Role", "Poin" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addUser());
        btnEdit.addActionListener(e -> editUser());
        btnDelete.addActionListener(e -> deleteUser());
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
        List<User> list = controller.getAllUsers();
        for (User u : list) {
            tableModel.addRow(new Object[] {
                    u.getId(), u.getNama(), u.getEmail(), u.getTelepon(),
                    u.getUsername(), u.getRole(), u.getSaldoPoin()
            });
        }
    }

    private void loadSelection() {
        int row = table.getSelectedRow();
        txtNama.setText(tableModel.getValueAt(row, 1).toString());
        txtEmail.setText(tableModel.getValueAt(row, 2).toString());
        txtTelepon.setText(tableModel.getValueAt(row, 3).toString());
        txtUsername.setText(tableModel.getValueAt(row, 4).toString());
        // Password not shown in table, leave empty or placeholder
        txtPassword.setText("");
        cmbRole.setSelectedItem(tableModel.getValueAt(row, 5).toString());
    }

    private void clearForm() {
        txtNama.setText("");
        txtEmail.setText("");
        txtTelepon.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cmbRole.setSelectedIndex(0);
        table.clearSelection();
    }

    private void addUser() {
        if (controller.addUser(txtNama.getText(), txtEmail.getText(), txtTelepon.getText(),
                txtUsername.getText(), txtPassword.getText(), cmbRole.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(this, "Berhasil");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal (Username/Email mungkin duplikat)");
        }
    }

    private void editUser() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) tableModel.getValueAt(row, 0);
        // Note: Controller.updateUser doesn't update password in my implementation,
        // only profile info.
        if (controller.updateUser(id, txtNama.getText(), txtEmail.getText(), txtTelepon.getText(),
                txtUsername.getText(), cmbRole.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(this, "Berhasil update info (password tidak berubah)");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal");
        }
    }

    private void deleteUser() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        if (JOptionPane.showConfirmDialog(this, "Yakin?") == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            if (controller.deleteUser(id)) {
                JOptionPane.showMessageDialog(this, "Berhasil");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal");
            }
        }
    }
}
