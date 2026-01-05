package com.ewaste.models;

import java.sql.Timestamp;

public class User {
    private int id;
    private String nama;
    private String email;
    private String telepon;
    private String username;
    private String password;
    private String role;
    private int saldoPoin;
    private Timestamp createdAt;

    public User() {
    }

    public User(int id, String nama, String email, String telepon, String username, String password, String role,
            int saldoPoin) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.telepon = telepon;
        this.username = username;
        this.password = password;
        this.role = role;
        this.saldoPoin = saldoPoin;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getSaldoPoin() {
        return saldoPoin;
    }

    public void setSaldoPoin(int saldoPoin) {
        this.saldoPoin = saldoPoin;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
