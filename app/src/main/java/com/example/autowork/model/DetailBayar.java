package com.example.autowork.model;

public class DetailBayar {
    private String namaKasir;
    private String timestamp;
    private String uid;

    public DetailBayar(String namaKasir, String timestamp, String uid) {
        this.namaKasir = namaKasir;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    public String getNamaKasir() {
        return namaKasir;
    }

    public void setNamaKasir(String namaKasir) {
        this.namaKasir = namaKasir;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return  " "+namaKasir+"\n" +
                " "+timestamp+"\n" +
                " "+uid;

    }
}