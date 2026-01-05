package com.ewaste.controllers;

import com.ewaste.config.DatabaseConfig;
import com.ewaste.models.Lokasi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LokasiController {

    public boolean addLokasi(String nama, String alamat, String kota) {
        if (nama == null || nama.isEmpty() || alamat == null || kota == null)
            return false;

        String sql = "INSERT INTO lokasi (nama_lokasi, alamat, kota) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nama);
            pstmt.setString(2, alamat);
            pstmt.setString(3, kota);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Lokasi> getAllLokasi() {
        List<Lokasi> list = new ArrayList<>();
        String sql = "SELECT * FROM lokasi ORDER BY id ASC";
        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Lokasi l = new Lokasi(
                        rs.getInt("id"),
                        rs.getString("nama_lokasi"),
                        rs.getString("alamat"),
                        rs.getString("kota"));
                l.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateLokasi(int id, String nama, String alamat, String kota) {
        if (nama == null || nama.isEmpty())
            return false;

        String sql = "UPDATE lokasi SET nama_lokasi = ?, alamat = ?, kota = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nama);
            pstmt.setString(2, alamat);
            pstmt.setString(3, kota);
            pstmt.setInt(4, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteLokasi(int id) {
        String sql = "DELETE FROM lokasi WHERE id = ?";
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
