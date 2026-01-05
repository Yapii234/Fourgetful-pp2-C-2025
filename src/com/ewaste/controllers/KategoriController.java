package com.ewaste.controllers;

import com.ewaste.config.DatabaseConfig;
import com.ewaste.models.Kategori;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KategoriController {

    public boolean addKategori(String nama, String deskripsi, double harga) {
        if (nama == null || nama.isEmpty() || harga <= 0)
            return false;

        String sql = "INSERT INTO kategori (nama_kategori, deskripsi, harga_per_kg) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nama);
            pstmt.setString(2, deskripsi);
            pstmt.setDouble(3, harga);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Kategori> getAllKategori() {
        List<Kategori> list = new ArrayList<>();
        String sql = "SELECT * FROM kategori ORDER BY id ASC";
        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Kategori k = new Kategori(
                        rs.getInt("id"),
                        rs.getString("nama_kategori"),
                        rs.getString("deskripsi"),
                        rs.getDouble("harga_per_kg"));
                k.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(k);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateKategori(int id, String nama, String deskripsi, double harga) {
        if (nama == null || nama.isEmpty() || harga <= 0)
            return false;

        String sql = "UPDATE kategori SET nama_kategori = ?, deskripsi = ?, harga_per_kg = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nama);
            pstmt.setString(2, deskripsi);
            pstmt.setDouble(3, harga);
            pstmt.setInt(4, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteKategori(int id) {
        String sql = "DELETE FROM kategori WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Likely foreign key constraint if transaction exists
            e.printStackTrace();
            return false;
        }
    }
}
