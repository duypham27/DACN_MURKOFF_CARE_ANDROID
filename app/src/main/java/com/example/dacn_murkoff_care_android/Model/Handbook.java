package com.example.dacn_murkoff_care_android.Model;

public class Handbook {

    private String image;
    private String title;
    private String url;

    public Handbook() {
    }

    public Handbook(String image, String title, String url) {
        this.image = image;
        this.title = title;
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

}
