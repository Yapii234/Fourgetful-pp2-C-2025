package com.ewaste.models;

import java.sql.Timestamp;

public class Lokasi {
    private int id;
    private String namaLokasi;
    private String alamat;
    private String kota;
    private Timestamp createdAt;

    public Lokasi() {
    }

    public Lokasi(int id, String namaLokasi, String alamat, String kota) {
        this.id = id;
        this.namaLokasi = namaLokasi;
        this.alamat = alamat;
        this.kota = kota;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaLokasi() {
        return namaLokasi;
    }

    public void setNamaLokasi(String namaLokasi) {
        this.namaLokasi = namaLokasi;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return namaLokasi + " - " + kota;
    }
}
