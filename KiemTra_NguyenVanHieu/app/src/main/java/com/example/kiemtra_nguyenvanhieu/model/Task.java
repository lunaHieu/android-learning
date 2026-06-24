package com.example.kiemtra_nguyenvanhieu.model;

import java.io.Serializable;

public class Task implements Serializable {
    private int ma;
    private String tieuDe;
    private String moTa;
    private String loaiCongViec;
    private String ngayTao;
    private int trangThai; // 0: chưa xong, 1: hoàn thành

    // Constructor khi lấy từ Database (có mã)
    public Task(int ma, String tieuDe, String moTa, String loaiCongViec, String ngayTao, int trangThai) {
        this.ma = ma;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.loaiCongViec = loaiCongViec;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
    }

    // Constructor khi thêm mới (chưa có mã)
    public Task(String tieuDe, String moTa, String loaiCongViec, String ngayTao, int trangThai) {
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.loaiCongViec = loaiCongViec;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
    }

    // --- Getters & Setters ---
    public int getMa() { return ma; }
    public void setMa(int ma) { this.ma = ma; }
    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public String getLoaiCongViec() { return loaiCongViec; }
    public void setLoaiCongViec(String loaiCongViec) { this.loaiCongViec = loaiCongViec; }
    public String getNgayTao() { return ngayTao; }
    public void setNgayTao(String ngayTao) { this.ngayTao = ngayTao; }
    public int getTrangThai() { return trangThai; }
    public void setTrangThai(int trangThai) { this.trangThai = trangThai; }
}