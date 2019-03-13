package com.rawzadigital.masjidnet.model;

public class Slidder {
    public String title;
    private String date;
    private String url;
    private String thumbnail;

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String geturl() {
        return url;
    }


    public void seturl(String url) {
        this.url = url;
    }


    public String getImage() {
        return thumbnail;
    }

    public void setImage(String thumbnail) {
        this.thumbnail = thumbnail;

    }
}
