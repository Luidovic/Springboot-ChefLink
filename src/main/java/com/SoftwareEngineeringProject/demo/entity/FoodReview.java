package com.SoftwareEngineeringProject.demo.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "FoodReview")
public class FoodReview {
    private double rating;
    private String comment;
    private String id_user;
    private String id_food;

    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getID_User() {
        return id_user;
    }
    public void setID_User(String id_user) {
        this.id_user = id_user;
    }
    public String getId_food() {
        return id_food;
    }
    public void setId_food(String id_food) {
        this.id_food = id_food;
    }
    public FoodReview(double rating, String comment, String id_user, String id_food) {
        this.rating = rating;
        this.comment = comment;
        this.id_user = id_user;
        this.id_food = id_food;
    }

}
