package com.ewaste.models;

import java.sql.Date;
import java.sql.Timestamp;

public class Transaksi {
    private int id;
    private int idUser;
    private int idKategori;
    private int idLokasi;
    private double berat;
    private double totalHarga;
    private Date tanggalTransaksi;
    private String catatan;
    private String status;
    private Timestamp createdAt;

    // Additional fields for display (Joins)
    private String namaUser;
    private String namaKategori;
    private String namaLokasi;

    public Transaksi() {
    }

    public Transaksi(int id, int idUser, int idKategori, int idLokasi, double berat, double totalHarga,
            Date tanggalTransaksi, String catatan, String status) {
        this.id = id;
        this.idUser = idUser;
        this.idKategori = idKategori;
        this.idLokasi = idLokasi;
        this.berat = berat;
        this.totalHarga = totalHarga;
        this.tanggalTransaksi = tanggalTransaksi;
        this.catatan = catatan;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdKategori() {
        return idKategori;
    }

    public void setIdKategori(int idKategori) {
        this.idKategori = idKategori;
    }

    public int getIdLokasi() {
        return idLokasi;
    }

    public void setIdLokasi(int idLokasi) {
        this.idLokasi = idLokasi;
    }

    public double getBerat() {
        return berat;
    }

    public void setBerat(double berat) {
        this.berat = berat;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public Date getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(Date tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }

    public String getNamaLokasi() {
        return namaLokasi;
    }

    public void setNamaLokasi(String namaLokasi) {
        this.namaLokasi = namaLokasi;
    }
}
