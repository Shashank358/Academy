package com.iiysoftware.academy.Models;

public class AssignedStudents {

    private String name;
    private String email;
    private String profile;
    private String uid;
    private boolean isAssigned;
    private String driver_id;
    private String driver_name;
    private String driver_image;

    public AssignedStudents(String name, String email, String profile, String uid, boolean isAssigned, String driver_id, String driver_name, String driver_image) {
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.uid = uid;
        this.isAssigned = isAssigned;
        this.driver_id = driver_id;
        this.driver_name = driver_name;
        this.driver_image = driver_image;
    }

    public AssignedStudents(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_image() {
        return driver_image;
    }

    public void setDriver_image(String driver_image) {
        this.driver_image = driver_image;
    }
}
