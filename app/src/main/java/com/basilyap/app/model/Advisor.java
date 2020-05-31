package com.basilyap.app.model;

public class Advisor {

    private int id;
    private String persian;
    private String english;
    private String title;
    private String email;
    private String phone;
    private String image;

    public Advisor(int id, String persian, String english, String title, String email, String phone, String image) {
        this.id = id;
        this.persian = persian;
        this.english = english;
        this.title = title;
        this.email = email;
        this.phone = phone;
        this.image = image;
    }

    public Advisor() {
    }

    public int getId() {
        return id;
    }

    public String getPersian() {
        return persian;
    }

    public String getEnglish() {
        return english;
    }

    public String getTitle() {
        return title;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }
}
