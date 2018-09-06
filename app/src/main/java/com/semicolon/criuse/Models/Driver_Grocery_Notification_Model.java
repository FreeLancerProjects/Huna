package com.semicolon.criuse.Models;

import java.io.Serializable;
import java.util.List;

public class Driver_Grocery_Notification_Model implements Serializable {
    private String id_delivery_order;
    private String id_client_fk;
    private String bill_num_fk;
    private String is_read;
    private String client_name;
    private String client_phone;
    private String client_city;
    private String client_photo;
    private String bill_address;
    private String bill_google_lat;
    private String bill_google_long;
    private String delivery_order_time;
    private int    bill_cost;
    private List<Bill_Product> bill_product;

    public String getId_delivery_order() {
        return id_delivery_order;
    }

    public String getId_client_fk() {
        return id_client_fk;
    }

    public String getBill_num_fk() {
        return bill_num_fk;
    }

    public String getIs_read() {
        return is_read;
    }

    public String getClient_name() {
        return client_name;
    }

    public String getClient_phone() {
        return client_phone;
    }

    public String getClient_city() {
        return client_city;
    }

    public String getClient_photo() {
        return client_photo;
    }

    public String getBill_address() {
        return bill_address;
    }

    public String getBill_google_lat() {
        return bill_google_lat;
    }

    public String getBill_google_long() {
        return bill_google_long;
    }

    public String getDelivery_order_time() {
        return delivery_order_time;
    }

    public int getBill_cost() {
        return bill_cost;
    }

    public List<Bill_Product> getBill_product() {
        return bill_product;
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
