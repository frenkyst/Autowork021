package com.example.autowork.model;

public class UpdateStokMasuk {

    private String barkod;
    private String nama;
    private String jml;
    private String timestamp;

    public UpdateStokMasuk(String barkod, String nama, String jml, String timestamp) {
        this.barkod = barkod;
        this.nama = nama;
        this.jml = jml;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return " "+barkod+"\n" +
                " "+jml+"\n" +
                " "+nama+"\n" +
                " "+timestamp;
    }
}
