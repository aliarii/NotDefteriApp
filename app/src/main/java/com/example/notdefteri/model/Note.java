package com.example.notdefteri.model;

public class Note {
    private String title;
    private String content;
    private String password;
    private String date;
    public Note(){}
    public Note(String baslik,String icerik,String sifre,String tarih){
        this.title = baslik;
        this.content = icerik;
        this.password = sifre;
        this.date = tarih;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
