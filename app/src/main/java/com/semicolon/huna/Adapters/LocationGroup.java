package com.semicolon.huna.Adapters;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class LocationGroup extends ExpandableGroup {
    public String city_id;
    public LocationGroup(String title, List items ,String city_id)
    {
        super(title, items);
        this.city_id = city_id;
    }

    public String getCity_id() {
        return city_id;
    }
}
