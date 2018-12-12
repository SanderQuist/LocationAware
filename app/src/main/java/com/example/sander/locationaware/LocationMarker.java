package com.example.sander.locationaware;

public class LocationMarker {
    private String name;
    private String description;
    private long date;
    private double x;
    private double y;

    public LocationMarker(String name, String description, long date, double x, double y){
        this.name = name;
        this.description = description;
        this.date = date;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
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
}
