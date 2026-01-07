CREATE DATABASE IF NOT EXISTS e_waste_db;
USE e_waste_db;

-- 1. Tabel Users
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telepon VARCHAR(20) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Plain text as requested
    role ENUM('admin', 'user') NOT NULL DEFAULT 'user',
    saldo_poin INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabel Kategori
CREATE TABLE IF NOT EXISTS kategori (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_kategori VARCHAR(100) NOT NULL,
    deskripsi TEXT,
    harga_per_kg DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tabel Lokasi
CREATE TABLE IF NOT EXISTS lokasi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_lokasi VARCHAR(100) NOT NULL,
    alamat TEXT NOT NULL,
    kota VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Tabel Transaksi
CREATE TABLE IF NOT EXISTS transaksi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    id_kategori INT NOT NULL,
    id_lokasi INT NOT NULL,
    berat DOUBLE NOT NULL,
    total_harga DOUBLE NOT NULL,
    tanggal_transaksi DATE NOT NULL,
    catatan TEXT,
    status ENUM('pending', 'selesai', 'dibatalkan') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_user) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (id_kategori) REFERENCES kategori(id) ON DELETE CASCADE,
    FOREIGN KEY (id_lokasi) REFERENCES lokasi(id) ON DELETE CASCADE
);

-- Seed Data: Admin
INSERT INTO users (nama, email, telepon, username, password, role) 
VALUES ('Administrator', 'admin@ewaste.com', '08123456789', 'admin', 'admin123', 'admin');

-- Seed Data: User Demo
INSERT INTO users (nama, email, telepon, username, password, role, saldo_poin) 
VALUES ('User Demo', 'user@ewaste.com', '08987654321', 'user', 'user123', 'user', 0);

-- Seed Data: Kategori
INSERT INTO kategori (nama_kategori, deskripsi, harga_per_kg) VALUES 
('Elektronik Besar', 'Kulkas, Mesin Cuci, TV', 5000),
('Elektronik Kecil', 'HP, Laptop, Tablet', 15000),
('Kabel & Aksesoris', 'Kabel charger, earphone', 3000);

-- Seed Data: Lokasi
INSERT INTO lokasi (nama_lokasi, alamat, kota) VALUES 
('Bank Sampah Pusat', 'Jl. Sudirman No. 1', 'Jakarta'),
('Drop Point Tebet', 'Jl. Tebet Raya No. 10', 'Jakarta'),
('Drop Point Bekasi', 'Jl. Ahmad Yani No. 5', 'Bekasi');