package com.semicolon.criuse.Models;

import java.io.Serializable;

public class GroceryPart1 implements Serializable {
    private String uri;
    private String name;
    private String hour;
    private String phone;
    private String username;
    private String password;
    private double lat;
    private double lng;

    public GroceryPart1(String uri, String name, String hour, String phone, String username, String password, double lat, double lng) {
        this.uri = uri;
        this.name = name;
        this.hour = hour;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.lat = lat;
        this.lng = lng;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
