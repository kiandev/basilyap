package com.basilyap.app.model;

public class Weblog {

    private int id;
    private String title;
    private String text;
    private String date;
    private String image;

    public Weblog(int id, String title, String text, String date, String image) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.date = date;
        this.image = image;
    }

    public Weblog() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }
}
