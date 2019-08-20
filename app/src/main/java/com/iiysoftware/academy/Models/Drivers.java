package com.iiysoftware.academy.Models;

public class Drivers {

    private String image;
    private String push_id;
    private String search;
    private String user_email;
    private String user_name;

    public Drivers(String image, String push_id, String search, String user_email, String user_name) {
        this.image = image;
        this.push_id = push_id;
        this.search = search;
        this.user_email = user_email;
        this.user_name = user_name;
    }

    public Drivers(){

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPush_id() {
        return push_id;
    }

    public void setPush_id(String push_id) {
        this.push_id = push_id;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
