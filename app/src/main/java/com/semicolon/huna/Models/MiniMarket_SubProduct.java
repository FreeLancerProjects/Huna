package com.semicolon.huna.Models;

import java.io.Serializable;

public class MiniMarket_SubProduct implements Serializable {
    private String market_id_fk;
    private String id_categories;
    private String id_product;
    private String product_title;
    private String product_image;
    private String product_price;
    private String product_content;

    public String getMarket_id_fk() {
        return market_id_fk;
    }

    public String getId_categories() {
        return id_categories;
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

    public String getProduct_price() {
        return product_price;
    }

    public String getProduct_content() {
        return product_content;
    }
}
