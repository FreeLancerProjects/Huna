package com.semicolon.huna.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ClientOrderModel implements Serializable {
    private String bill_address;
    private String bill_id;
    private String bill_google_lat;
    private String bill_google_long;
    private String id_delivery_user_fk;
    private String room_id;
    private String client_id_fk;
    private String bill_state;
    private String delivery_user_name;
    private String delivery_user_phone;
    private String delivery_user_city;
    private String delivery_user_photo;
    private String delivery_order_time;
    private int bill_cost;
    @SerializedName("bill_product")
    private List<Bill_Product> bill_productList;

    public String getBill_address() {
        return bill_address;
    }

    public String getBill_id() {
        return bill_id;
    }

    public String getBill_google_lat() {
        return bill_google_lat;
    }

    public String getBill_google_long() {
        return bill_google_long;
    }

    public String getId_delivery_user_fk() {
        return id_delivery_user_fk;
    }

    public String getRoom_id() {
        return room_id;
    }

    public String getClient_id_fk() {
        return client_id_fk;
    }

    public String getBill_state() {
        return bill_state;
    }

    public String getDelivery_user_name() {
        return delivery_user_name;
    }

    public String getDelivery_user_phone() {
        return delivery_user_phone;
    }

    public String getDelivery_user_city() {
        return delivery_user_city;
    }

    public String getDelivery_user_photo() {
        return delivery_user_photo;
    }

    public String getDelivery_order_time() {
        return delivery_order_time;
    }

    public int getBill_cost() {
        return bill_cost;
    }

    public List<Bill_Product> getBill_productList() {
        return bill_productList;
    }

    public class Bill_Product implements Serializable
    {
        private String product_amount;
        private String product_cost;
        private String id_product;
        private String product_title;
        private String product_image;

        public String getProduct_amount() {
            return product_amount;
        }

        public String getProduct_cost() {
            return product_cost;
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
    }

}
