package com.semicolon.huna.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuperMarketModel implements Serializable {
    private String main_department_fk;
    private String id_categories;
    private String name_categories;
    private String image_categories;
    private String market_name;
    private String market_phone;
    private String delivery_cost;
    private String market_id_fk;

    @SerializedName("sub_product")
    private List<SubProduct> subProductList;


    public String getMain_department_fk() {
        return main_department_fk;
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

    public String getMarket_name() {
        return market_name;
    }

    public String getMarket_phone() {
        return market_phone;
    }

    public String getDelivery_cost() {
        return delivery_cost;
    }

    public String getMarket_id_fk() {
        return market_id_fk;
    }

    public List<SubProduct> getSubProductList() {
        return subProductList;
    }



    public class SubProduct
    {
        private String market_id_fk;
        private String id_product;
        private String product_title;
        private String product_image;
        private String product_price;
        private String is_admin;

        public String getMarket_id_fk() {
            return market_id_fk;
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

        public String getIs_admin() {
            return is_admin;
        }
    }
}
