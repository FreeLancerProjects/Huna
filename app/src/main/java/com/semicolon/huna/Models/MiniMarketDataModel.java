package com.semicolon.huna.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MiniMarketDataModel implements Serializable {
    private String market_id_fk;
    private String market_name;
    private String market_phone;
    private String market_google_lat;
    private String market_google_long;
    private String market_city;
    private String market_photo;
    private String delivery_cost;
    private double distance;
    @SerializedName("sub_categories")
    private List<MiniMarket_SubCategory> miniMarket_subCategoryList;

    public String getMarket_id_fk() {
        return market_id_fk;
    }

    public String getMarket_name() {
        return market_name;
    }

    public String getMarket_phone() {
        return market_phone;
    }

    public String getMarket_google_lat() {
        return market_google_lat;
    }

    public String getMarket_google_long() {
        return market_google_long;
    }

    public String getMarket_city() {
        return market_city;
    }

    public String getDelivery_cost() {
        return delivery_cost;
    }

    public String getMarket_photo() {
        return market_photo;
    }

    public double getDistance() {
        return distance;
    }

    public List<MiniMarket_SubCategory> getMiniMarket_subCategoryList() {
        return miniMarket_subCategoryList;
    }
}
