package com.semicolon.huna.Models;

import java.io.Serializable;

public class Bill_Model implements Serializable{
    private String name;
    private String phone;
    private int subtotal;
    private int delivery;
    private int total;

    public Bill_Model(String name, String phone, int subtotal, int delivery, int total) {
        this.name = name;
        this.phone = phone;
        this.subtotal = subtotal;
        this.delivery = delivery;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
