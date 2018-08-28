package com.semicolon.criuse.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MiniMarket_SubCategory implements Serializable {
    private String market_id_fk;
    private String id_categories;
    private String name_categories;
    private String image_categories;
    private String market_phone;
    private String delivery_cost;
    private String market_name;

    @SerializedName("sub_product")
    private List<MiniMarket_SubProduct> miniMarket_subProductList;

    public String getMarket_id_fk() {
        return market_id_fk;
    }

    public String getId_categories() {
        return id_categories;
    }

    public String getName_categories() {
        return name_categories;
    }

    public String getImage_categories() {
        return image_categories;
    }

    public void setMarket_phone(String market_phone) {
        this.market_phone = market_phone;
    }

    public void setDelivery_cost(String delivery_cost) {
        this.delivery_cost = delivery_cost;
    }

    public void setMarket_name(String market_name) {
        this.market_name = market_name;
    }

    public List<MiniMarket_SubProduct> getMiniMarket_subProductList() {
        return miniMarket_subProductList;
    }
}
