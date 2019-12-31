package com.example.autowork.model;

public class TransaksiKaryawan {
    private String barkod;
    private String nama;
    private String jml;
    private String namaKaryawan;
    private String uid;
    private String timestamp;

    public TransaksiKaryawan(String barkod, String nama, String jml, String namaKaryawan, String uid, String timestamp) {
        this.barkod = barkod;
        this.nama = nama;
        this.jml = jml;
        this.namaKaryawan = namaKaryawan;
        this.uid = uid;
        this.timestamp = timestamp;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJml() {
        return jml;
    }

    public void setJml(String jml) {
        this.jml = jml;
    }

    public String getNamaKaryawan() {
        return namaKaryawan;
    }

    public void setNamaKaryawan(String namaKaryawan) {
        this.namaKaryawan = namaKaryawan;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return " "+barkod+"\n" +
                " "+nama+"\n" +
                " "+jml+"\n" +
                " "+namaKaryawan+"\n"+
                " "+uid+"\n"+
                " "+timestamp;
    }
}
