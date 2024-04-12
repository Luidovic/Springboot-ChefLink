package com.SoftwareEngineeringProject.demo.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.SoftwareEngineeringProject.demo.subEntity.FoodCategory;
import com.SoftwareEngineeringProject.demo.subEntity.FoodOption;

@Document(collection = "Food")
public class Food {
    private String id_food;
    private String name;
    private double price;
    private double total_rating;
    private String picture;
    private String timing;
    private List<String> ingredients;
    private String description;
    private List<FoodOption> options;
    private FoodCategory category;

    private int ratings_count;

    public int getRatings_count() {
        return ratings_count;
    }

    public void setRatings_count(int ratings_count) {
        this.ratings_count = ratings_count;
    }

    public Food(String id_food, String name, double price, double total_rating, String picture, String timing,
            List<String> ingredients,
            String description,
            List<FoodOption> options, FoodCategory category) {
        this.id_food = id_food;
        this.name = name;
        this.price = price;
        this.picture = picture;
        this.ingredients = ingredients;
        this.description = description;
        this.options = options;
        this.total_rating = total_rating;
        this.category = category;
        this.timing = timing;
    }

    public Food() {
    }

    public String getId_food() {
        return id_food;
    }

    public void setId_food(String id_food) {
        this.id_food = id_food;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FoodOption> getOptions() {
        return options;
    }

    public void setOptions(List<FoodOption> options) {
        this.options = options;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public double getTotal_rating() {
        return total_rating;
    }

    public void setTotal_rating(double total_rating) {
        this.total_rating = total_rating;
    }

    @Override
    public String toString() {
        return "Food [id_food=" + id_food + ", name=" + name + ", price=" + price + ", total_rating=" + total_rating
                + ", picture=" + picture + ", timing=" + timing + ", ingredients=" + ingredients + ", description="
                + description + ", options=" + options + ", category=" + category + "]";
    }
}
