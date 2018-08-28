package com.semicolon.criuse.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PlaceName implements Serializable{

    @SerializedName("results")
    List<Address> results;

    public List<Address> getResults() {
        return results;
    }

    public class Address implements Serializable
    {
        private String formatted_address;

        public String getFormatted_address() {
            return formatted_address;
        }
    }
}
