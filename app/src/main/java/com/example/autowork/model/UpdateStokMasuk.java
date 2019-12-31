package com.example.autowork.model;

public class UpdateStokMasuk {

    private String barkod;
    private String jml;

    public UpdateStokMasuk(String barkod, String jml) {
        this.barkod = barkod;
        this.jml = jml;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public String getJml() {
        return jml;
    }

    public void setJml(String jml) {
        this.jml = jml;
    }

    @Override
    public String toString() {
        return " "+barkod+"\n" +
                " "+jml;
    }
}
