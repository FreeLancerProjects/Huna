package com.semicolon.huna.Models;

import java.io.Serializable;

public class SubProduct implements Serializable {
    private String id_product;
    private String product_title;
    private String product_image;
    private String main_department_fk;
    private String product_price;
    private String product_code;
    private String product_content;
    private String product_type;
    private String google_lat;
    private String google_long;
    private String image_categories;


    public SubProduct(String id_product, String product_title, String product_image, String main_department_fk, String product_price, String product_code, String product_content, String product_type, String google_lat, String google_long, String image_categories) {
        this.id_product = id_product;
        this.product_title = product_title;
        this.product_image = product_image;
        this.main_department_fk = main_department_fk;
        this.product_price = product_price;
        this.product_code = product_code;
        this.product_content = product_content;
        this.product_type = product_type;
        this.google_lat = google_lat;
        this.google_long = google_long;
        this.image_categories = image_categories;
    }

    public String getId_product() {
        return id_product;
    }

    public String getProduct_title() {
        return product_title;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getMain_department_fk() {
        return main_department_fk;
    }

    public String getProduct_price() {
        return product_price;
    }

    public String getProduct_code() {
        return product_code;
    }

    public String getProduct_content() {
        return product_content;
    }

    public String getProduct_type() {
        return product_type;
    }

    public String getGoogle_lat() {
        return google_lat;
    }

    public String getGoogle_long() {
        return google_long;
    }

    public String getImage_categories() {
        return image_categories;
    }

    public void setImage_categories(String image_categories) {
        this.image_categories = image_categories;
    }
}
