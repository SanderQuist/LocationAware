package com.example.sander.locationaware.Models;

public class LocationMarker {
    private String name;
    private String description;
    private String enDescription;
    private String  price;
    private double x;
    private double y;

    public LocationMarker(String name, String description, String enDescription, String  price, double x, double y){
        this.name = name;
        this.description = description;
        this.enDescription = enDescription;
        this.price = price;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String  price) {
        this.price = price;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getEnDescription() {
        return enDescription;
    }

    public void setEnDescription(String enDescription) {
        this.enDescription = enDescription;
    }
}
