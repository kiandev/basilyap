package com.basilyap.app.model;

public class Notification {

    private int id;
    private String title;
    private String text;
    private String date;

    public Notification(int id, String title, String text, String date) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public Notification() {
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
}
