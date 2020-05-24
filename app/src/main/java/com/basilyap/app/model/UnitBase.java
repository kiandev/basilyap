package com.basilyap.app.model;

public class UnitBase {

    private int id;
    private int unit_id;
    private String name;
    private int price;
    private String image;
    private String type;
    private String region;
    private int project_id;

    public UnitBase(int id, int unit_id, String name, int price, String image, String type, String region, int project_id) {
        this.id = id;
        this.unit_id = unit_id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.type = type;
        this.region = region;
        this.project_id = project_id;
    }

    public UnitBase() {
    }

    public int getId() {
        return id;
    }

    public int getUnit_id() {
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

    public int getProject_id() {
        return project_id;
    }
}
