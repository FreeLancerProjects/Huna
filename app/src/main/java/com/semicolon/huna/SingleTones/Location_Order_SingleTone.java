package com.semicolon.huna.SingleTones;

import com.google.android.gms.maps.model.LatLng;

public class Location_Order_SingleTone {
    private static Location_Order_SingleTone instance=null;
    private double order_lat=0.0,order_lng=0.0;
    private String address="";

    private Location_Order_SingleTone() {
    }

    public static synchronized Location_Order_SingleTone getInstance()
    {
        if (instance==null)
        {
            instance = new Location_Order_SingleTone();
        }
        return instance;
    }

    public void setOrderLocation(double order_lat,double order_lng,String address)
    {
        this.order_lat=order_lat;
        this.order_lng =order_lng;
        this.address = address;
    }

    public LatLng getOrderLocation()
    {
        return new LatLng(order_lat,order_lng);
    }

    public String getAddress() {
        return address;
    }

    public void clearLocation()
    {
        order_lat=order_lng=0.0;
        address="";
    }
}
