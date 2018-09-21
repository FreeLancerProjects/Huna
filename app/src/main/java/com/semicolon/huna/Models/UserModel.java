package com.semicolon.huna.Models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String user_id;
    private String user_name;
    private String user_type;
    private String user_full_name;
    private String user_phone;
    private String user_email;
    private String user_photo;
    private String user_token_id;
    private String user_google_lat;
    private String user_google_long;
    private String user_city;
    private String city_title;
    private String area_title;
    private String user_license_photo;
    private String user_car_photo;
    private String user_residence_photo;
    private String user_neighborhood;
    private String user_work_hours;
    private int success;

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getUser_full_name() {
        return user_full_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public String getUser_token_id() {
        return user_token_id;
    }

    public String getUser_google_lat() {
        return user_google_lat;
    }

    public String getUser_google_long() {
        return user_google_long;
    }

    public String getUser_city() {
        return user_city;
    }

    public String getUser_license_photo() {
        return user_license_photo;
    }

    public String getUser_car_photo() {
        return user_car_photo;
    }

    public String getUser_residence_photo() {
        return user_residence_photo;
    }

    public String getUser_neighborhood() {
        return user_neighborhood;
    }

    public String getUser_work_hours() {
        return user_work_hours;
    }

    public String getCity_title() {
        return city_title;
    }

    public String getArea_title() {
        return area_title;
    }

    public int getSuccess() {
        return success;
    }
}
