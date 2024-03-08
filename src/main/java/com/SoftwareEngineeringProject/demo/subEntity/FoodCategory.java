package com.SoftwareEngineeringProject.demo.subEntity;

import java.util.List;

public class FoodCategory {
    private String cuisine;
    private String type;
    private List<String> perks;

    public FoodCategory(String cuisine, String type, List<String> perks) {
        this.cuisine = cuisine;
        this.type = type;
        this.perks = perks;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getPerks() {
        return perks;
    }

    public void setPerks(List<String> perks) {
        this.perks = perks;
    }

}
