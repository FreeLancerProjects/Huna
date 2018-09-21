package com.semicolon.huna.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AllGroceries_SubCategory implements Serializable {
    private String id_categories;
    private String name_categories;
    private String image_categories;
    @SerializedName("sub_product")
    private List<SubProduct> subProducts;

    public String getId_categories() {
        return id_categories;
    }

    public String getName_categories() {
        return name_categories;
    }

    public String getImage_categories() {
        return image_categories;
    }

    public List<SubProduct> getSubProducts() {
        return subProducts;
    }

    public void setId_categories(String id_categories) {
        this.id_categories = id_categories;
    }

    public void setName_categories(String name_categories) {
        this.name_categories = name_categories;
    }

    public void setImage_categories(String image_categories) {
        this.image_categories = image_categories;
    }

    public void setSubProducts(List<SubProduct> subProducts) {
        this.subProducts = subProducts;
    }


}
