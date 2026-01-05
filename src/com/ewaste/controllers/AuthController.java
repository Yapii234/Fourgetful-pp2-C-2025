package com.ewaste.controllers;

import com.ewaste.config.DatabaseConfig;
import com.ewaste.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {

    // Session management (Simple static variable for logged in user)
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }

    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setNama(rs.getString("nama"));
                    user.setEmail(rs.getString("email"));
                    user.setTelepon(rs.getString("telepon"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    user.setSaldoPoin(rs.getInt("saldo_poin"));
                    user.setCreatedAt(rs.getTimestamp("created_at"));

                    currentUser = user; // Set session
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(String nama, String email, String telepon, String username, String password) {
        String sql = "INSERT INTO users (nama, email, telepon, username, password, role) VALUES (?, ?, ?, ?, ?, 'user')";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nama);
            pstmt.setString(2, email);
            pstmt.setString(3, telepon);
            pstmt.setString(4, username);
            pstmt.setString(5, password);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
