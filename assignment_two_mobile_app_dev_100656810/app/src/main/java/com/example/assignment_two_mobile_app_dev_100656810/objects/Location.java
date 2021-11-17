package com.example.assignment_two_mobile_app_dev_100656810.objects;

public class Location {
    private int id;
    private String addr;
    private Double latitude;
    private Double longitude;

    public Location(int id, String addr, Double latitude, Double longitude) {
        this.id = id;
        this.addr = addr;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public String getAddr() {
        return addr;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
