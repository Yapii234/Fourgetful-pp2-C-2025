package com.ewaste.controllers;

import com.ewaste.config.DatabaseConfig;
import com.ewaste.models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    public boolean addUser(String nama, String email, String telepon, String username, String password, String role) {
        // Simple validation
        if (username.isEmpty() || password.isEmpty())
            return false;

        String sql = "INSERT INTO users (nama, email, telepon, username, password, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nama);
            pstmt.setString(2, email);
            pstmt.setString(3, telepon);
            pstmt.setString(4, username);
            pstmt.setString(5, password);
            pstmt.setString(6, role);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle unique constraints (email/username)
            return false;
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setNama(rs.getString("nama"));
                u.setEmail(rs.getString("email"));
                u.setTelepon(rs.getString("telepon"));
                u.setUsername(rs.getString("username"));
                // Don't expose password
                u.setRole(rs.getString("role"));
                u.setSaldoPoin(rs.getInt("saldo_poin"));
                u.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateUser(int id, String nama, String email, String telepon, String username, String role) {
        // Note: Password update is usually separate, here we keep it simple (not
        // updating password here)
        String sql = "UPDATE users SET nama = ?, email = ?, telepon = ?, username = ?, role = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nama);
            pstmt.setString(2, email);
            pstmt.setString(3, telepon);
            pstmt.setString(4, username);
            pstmt.setString(5, role);
            pstmt.setInt(6, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
