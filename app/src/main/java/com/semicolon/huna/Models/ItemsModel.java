package com.semicolon.huna.Models;

import java.io.Serializable;

public class ItemsModel implements Serializable {
    private String user_id;
    private String id_categories;
    private String id_product;
    private String product_cost;
    private String product_amount;
    private String market_id_fk;
    private String is_admin;
    private String product_image;
    private String product_name;
    private String department;
    private String market_name;
    private String market_phone;
    private String delivery_cost;
    private String item_one_cost;
    private String market_photo;
    private double user_google_lat;
    private double user_google_long;
    private String user_address;

    public ItemsModel(String user_id, String id_categories, String id_product, String product_cost, String product_amount, String market_id_fk, String is_admin, String product_image, String product_name, String department, String market_name, String market_phone, String delivery_cost, String item_one_cost) {
        this.user_id = user_id;
        this.id_categories = id_categories;
        this.id_product = id_product;
        this.product_cost = product_cost;
        this.product_amount = product_amount;
        this.market_id_fk = market_id_fk;
        this.is_admin = is_admin;
        this.product_image = product_image;
        this.product_name = product_name;
        this.department = department;
        this.market_name = market_name;
        this.market_phone = market_phone;
        this.delivery_cost = delivery_cost;
        this.item_one_cost = item_one_cost;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId_categories() {
        return id_categories;
    }

    public void setId_categories(String id_categories) {
        this.id_categories = id_categories;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getProduct_cost() {
        return product_cost;
    }

    public void setProduct_cost(String product_cost) {
        this.product_cost = product_cost;
    }

    public String getProduct_amount() {
        return product_amount;
    }

    public void setProduct_amount(String product_amount) {
        this.product_amount = product_amount;
    }

    public String getMarket_id_fk() {
        return market_id_fk;
    }

    public void setMarket_id_fk(String market_id_fk) {
        this.market_id_fk = market_id_fk;
    }

    public String getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMarket_name() {
        return market_name;
    }

    public void setMarket_name(String market_name) {
        this.market_name = market_name;
    }

    public String getMarket_phone() {
        return market_phone;
    }

    public void setMarket_phone(String market_phone) {
        this.market_phone = market_phone;
    }

    public String getDelivery_cost() {
        return delivery_cost;
    }

    public void setDelivery_cost(String delivery_cost) {
        this.delivery_cost = delivery_cost;
    }

    public String getItem_one_cost() {
        return item_one_cost;
    }

    public void setItem_one_cost(String item_one_cost) {
        this.item_one_cost = item_one_cost;
    }

    public double getUser_google_lat() {
        return user_google_lat;
    }

    public void setUser_google_lat(double user_google_lat) {
        this.user_google_lat = user_google_lat;
    }

    public double getUser_google_long() {
        return user_google_long;
    }

    public void setUser_google_long(double user_google_long) {
        this.user_google_long = user_google_long;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getMarket_photo() {
        return market_photo;
    }
}
