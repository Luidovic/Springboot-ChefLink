package com.SoftwareEngineeringProject.demo.helperEntity;

import java.util.List;

import com.SoftwareEngineeringProject.demo.entity.Food;

public class CuisineGroup {
    private String id;
    private List<Food> collections;

    public CuisineGroup() {
    }

    public CuisineGroup(String id, List<Food> collections) {
        this.id = id;
        this.collections = collections;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Food> getCollections() {
        return collections;
    }

    public void setCollections(List<Food> collections) {
        this.collections = collections;
    }

    @Override
    public String toString() {
        return "CuisineGroup [id=" + id + ", collections=" + collections + "]";
    }
}
