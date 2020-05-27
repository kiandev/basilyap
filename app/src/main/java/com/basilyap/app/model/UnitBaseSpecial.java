package com.basilyap.app.model;

public class UnitBaseSpecial {

    private int id;
    private String unit_id;
    private String name;
    private int price;
    private String image;
    private String type;
    private String region;
    private String project_id;
    private int special;

    public UnitBaseSpecial(int id, String unit_id, String name, int price, String image, String type, String region, String project_id, int special) {
        this.id = id;
        this.unit_id = unit_id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.type = type;
        this.region = region;
        this.project_id = project_id;
        this.special = special;
    }

    public UnitBaseSpecial() {
    }

    public int getId() {
        return id;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getType() {
        return type;
    }

    public String getRegion() {
        return region;
    }

    public String getProject_id() {
        return project_id;
    }

    public int getSpecial() {
        return special;
    }
}
