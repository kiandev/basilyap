package com.basilyap.app.model;

public class UnitImage {

    private int id;
    private int unit_id;
    private String link;

    public UnitImage(int id, int unit_id, String link) {
        this.id = id;
        this.unit_id = unit_id;
        this.link = link;
    }

    public UnitImage() {
    }

    public int getId() {
        return id;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public String getLink() {
        return link;
    }
}
