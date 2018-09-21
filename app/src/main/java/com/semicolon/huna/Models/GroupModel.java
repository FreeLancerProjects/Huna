package com.semicolon.huna.Models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class GroupModel extends ExpandableGroup {
    private String image_url="";
    public GroupModel(String title, List items,String image_url) {
        super(title, items);
        this.image_url =image_url;
    }

    public String getImage_url() {
        return image_url;
    }
}
