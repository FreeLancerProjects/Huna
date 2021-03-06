package com.semicolon.huna.Models;

import java.io.Serializable;
import java.util.List;

public class CountryModel implements Serializable {
    private String city_id;
    private String city_title;
    private String from_id;
    private List<Neighborhood> sub_areas;

    public String getCity_id() {
        return city_id;
    }

    public String getCity_title() {
        return city_title;
    }

    public String getFrom_id() {
        return from_id;
    }

    public List<Neighborhood> getSub_areas() {
        return sub_areas;
    }

    public class Neighborhood implements Serializable
    {
        private String id_area;
        private String area_title;
        private String from_id;

        public String getId_area() {
            return id_area;
        }

        public String getArea_title() {
            return area_title;
        }

        public String getFrom_id() {
            return from_id;
        }
    }

}
