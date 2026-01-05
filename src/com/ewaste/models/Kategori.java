package com.ewaste.models;

import java.sql.Timestamp;

public class Kategori {
    private int id;
    private String namaKategori;
    private String deskripsi;
    private double hargaPerKg;
    private Timestamp createdAt;

    public Kategori() {
    }

    public Kategori(int id, String namaKategori, String deskripsi, double hargaPerKg) {
        this.id = id;
        this.namaKategori = namaKategori;
        this.deskripsi = deskripsi;
        this.hargaPerKg = hargaPerKg;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public double getHargaPerKg() {
        return hargaPerKg;
    }

    public void setHargaPerKg(double hargaPerKg) {
        this.hargaPerKg = hargaPerKg;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return namaKategori + " (Rp " + hargaPerKg + "/kg)";
    }
}
