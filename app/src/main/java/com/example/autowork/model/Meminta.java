package com.example.autowork.model;

public class Meminta {

    private String barkod;
    private String nama;
    private String jml;

    private String key;



    private String hargaawal;
    private String hargajual;

    private String total;

    private String timestamp;




    public Meminta() {
    }




    // MODEL INPUT DATA TRANSAKSI 1
//    public Meminta(String barkod, String nama, String jml, String total) {
//        this.barkod = barkod;
//        this.nama = nama;
//        this.jml = jml;
//        this.total = total;
//    }

    // MODEL INPUT DATA BARANG BARU YANG BELUM PERNAH ADA SEBELUMNYA DAN MENJADI ADA


    public Meminta(String barkod, String nama, String jml, String hargaawal, String hargajual, String timestamp) {
        this.barkod = barkod;
        this.nama = nama;
        this.jml = jml;
        this.hargaawal = hargaawal;
        this.hargajual = hargajual;
        this.timestamp = timestamp;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHargaawal() {
        return hargaawal;
    }

    public void setHargaawal(String hargaawal) {
        this.hargaawal = hargaawal;
    }

    public String getHargajual() {
        return hargajual;
    }

    public void setHargajual(String hargajual) {
        this.hargajual = hargajual;
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
                " "+hargaawal+"\n" +
                " "+hargajual+"\n" +
                " "+timestamp+"\n" +
                " "+total;
    }

    //*/

}
