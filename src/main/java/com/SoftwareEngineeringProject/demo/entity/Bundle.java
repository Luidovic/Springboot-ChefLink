package com.SoftwareEngineeringProject.demo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Bundle")
public class Bundle {
    private String id_bundle;
    private String id_food;
    private String id_options;
    private int quantity;
    private String special_instructions;

    public Bundle(String id_bundle, String id_food, String id_options, int quantity, String special_instructions) {
        this.id_bundle = id_bundle;
        this.id_food = id_food;
        this.id_options = id_options;
        this.quantity = quantity;
        this.special_instructions = special_instructions;
    }

    public String getId_bundle() {
        return id_bundle;
    }

    public void setId_bundle(String id_bundle) {
        this.id_bundle = id_bundle;
    }

    public String getId_food() {
        return id_food;
    }

    public void setId_food(String id_food) {
        this.id_food = id_food;
    }

    public String getId_options() {
        return id_options;
    }

    public void setId_options(String id_options) {
        this.id_options = id_options;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSpecial_instructions() {
        return special_instructions;
    }

    public void setSpecial_instructions(String special_instructions) {
        this.special_instructions = special_instructions;
    }

}
