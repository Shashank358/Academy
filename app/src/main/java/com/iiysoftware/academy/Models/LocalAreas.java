package com.iiysoftware.academy.Models;

import android.location.LocationListener;

public class LocalAreas {

    private String area_name;
    private String address;

    public LocalAreas(String area_name, String address) {
        this.area_name = area_name;
        this.address = address;
    }

    public LocalAreas(){

    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
