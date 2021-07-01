package com.example.notdefteri.model;

public class Note {
    private String baslik;
    private String icerik;
    private String sifre;
    private String tarih;
    public Note(){}
    public Note(String baslik,String icerik,String sifre,String tarih){
        this.baslik = baslik;
        this.icerik = icerik;
        this.sifre = sifre;
        this.tarih = tarih;
    }
    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getSifre(){
        return sifre;
    }
    public void setSifre(String sifre) {
        this.sifre = sifre;
    }
    public String getIcerik() {
        return icerik;
    }

    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }

}
