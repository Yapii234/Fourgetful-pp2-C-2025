package com.ewaste.controllers;

import com.ewaste.config.DatabaseConfig;
import com.ewaste.models.Transaksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaksiController {

    // Create Transaction (User)
    public boolean createTransaksi(int idUser, int idKategori, int idLokasi, double berat, String catatan) {
        // 1. Get Harga per Kg
        double hargaPerKg = 0;
        String sqlHarga = "SELECT harga_per_kg FROM kategori WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlHarga)) {
            pstmt.setInt(1, idKategori);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    hargaPerKg = rs.getDouble("harga_per_kg");
                } else {
                    return false; // Kategori not found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // 2. Calculate Total
        double totalHarga = hargaPerKg * berat;

        // 3. Insert Transaksi
        String sqlInsert = "INSERT INTO transaksi (id_user, id_kategori, id_lokasi, berat, total_harga, tanggal_transaksi, catatan, status) VALUES (?, ?, ?, ?, ?, CURDATE(), ?, 'pending')";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {

            pstmt.setInt(1, idUser);
            pstmt.setInt(2, idKategori);
            pstmt.setInt(3, idLokasi);
            pstmt.setDouble(4, berat);
            pstmt.setDouble(5, totalHarga);
            pstmt.setString(6, catatan);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get All Transactions (Admin History)
    public List<Transaksi> getAllTransaksi() {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT t.*, u.nama AS user_nama, k.nama_kategori, l.nama_lokasi " +
                "FROM transaksi t " +
                "JOIN users u ON t.id_user = u.id " +
                "JOIN kategori k ON t.id_kategori = k.id " +
                "JOIN lokasi l ON t.id_lokasi = l.id " +
                "ORDER BY t.created_at DESC";

        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Transaksi t = mapResultSetToTransaksi(rs);
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get Recent Transactions (Admin Dashboard - Limit 10)
    public List<Transaksi> getRecentTransaksi() {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT t.*, u.nama AS user_nama, k.nama_kategori, l.nama_lokasi " +
                "FROM transaksi t " +
                "JOIN users u ON t.id_user = u.id " +
                "JOIN kategori k ON t.id_kategori = k.id " +
                "JOIN lokasi l ON t.id_lokasi = l.id " +
                "ORDER BY t.created_at DESC LIMIT 10";

        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Transaksi t = mapResultSetToTransaksi(rs);
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get User Transactions (User History)
    public List<Transaksi> getUserTransaksi(int userId) {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT t.*, u.nama AS user_nama, k.nama_kategori, l.nama_lokasi " +
                "FROM transaksi t " +
                "JOIN users u ON t.id_user = u.id " +
                "JOIN kategori k ON t.id_kategori = k.id " +
                "JOIN lokasi l ON t.id_lokasi = l.id " +
                "WHERE t.id_user = ? " +
                "ORDER BY t.created_at DESC";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaksi t = mapResultSetToTransaksi(rs);
                    list.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Update Status (Admin) -> Update User Points if 'selesai'
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE transaksi SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, id);

            int affected = pstmt.executeUpdate();
            if (affected > 0 && "selesai".equalsIgnoreCase(status)) {
                // Add points logic (1000 Total Harga = 1 Point, for example)
                // First get transaction details
                updateUserPoints(id);
            }
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateUserPoints(int transaksiId) {
        String sqlGet = "SELECT id_user, total_harga FROM transaksi WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlGet)) {

            pstmt.setInt(1, transaksiId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("id_user");
                    double total = rs.getDouble("total_harga");
                    int pointsToAdd = (int) (total / 1000); // 1 point per 1000 rupiah

                    String sqlUpdate = "UPDATE users SET saldo_poin = saldo_poin + ? WHERE id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {
                        updateStmt.setInt(1, pointsToAdd);
                        updateStmt.setInt(2, userId);
                        updateStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cancel Transaction (User)
    public boolean cancelTransaksi(int id, int userId) {
        String sql = "UPDATE transaksi SET status = 'dibatalkan' WHERE id = ? AND id_user = ? AND status = 'pending'";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setInt(2, userId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Statistics for Dashboard
    public int getTotalTransaksi() {
        return getCount("SELECT COUNT(*) FROM transaksi");
    }

    public double getTotalBerat() {
        String sql = "SELECT SUM(berat) FROM transaksi WHERE status = 'selesai'";
        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getUserAktif() {
        return getCount("SELECT COUNT(*) FROM users WHERE role = 'user'");
    }

    public int getLokasiAktif() {
        return getCount("SELECT COUNT(*) FROM lokasi");
    }

    private int getCount(String sql) {
        try (Connection conn = DatabaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Transaksi mapResultSetToTransaksi(ResultSet rs) throws SQLException {
        Transaksi t = new Transaksi();
        t.setId(rs.getInt("id"));
        t.setIdUser(rs.getInt("id_user"));
        t.setIdKategori(rs.getInt("id_kategori"));
        t.setIdLokasi(rs.getInt("id_lokasi"));
        t.setBerat(rs.getDouble("berat"));
        t.setTotalHarga(rs.getDouble("total_harga"));
        t.setTanggalTransaksi(rs.getDate("tanggal_transaksi"));
        t.setCatatan(rs.getString("catatan"));
        t.setStatus(rs.getString("status"));
        t.setCreatedAt(rs.getTimestamp("created_at"));

        // Joined fields
        t.setNamaUser(rs.getString("user_nama"));
        t.setNamaKategori(rs.getString("nama_kategori"));
        t.setNamaLokasi(rs.getString("nama_lokasi"));
        return t;
    }
}
