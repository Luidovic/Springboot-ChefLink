package com.SoftwareEngineeringProject.demo.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Chef")
public class Chef {

    private String username;
    private String uUID;
    private String email;
    private String phone_Number;
    private String gender;
    private String firstName;
    private String lastName;
    private List<String> foodList;
    private String dateOfBirth;
    private List<String> bookmarks;
    private String p_URL;
    private List<String> locations;

    public Chef(String username, String uUID, String email, String phone_Number, String gender, String firstName,
            String lastName, List<String> foodList, String dateOfBirth, List<String> bookmarks, String p_URL,
            List<String> locations) {
        this.username = username;
        this.uUID = uUID;
        this.email = email;
        this.phone_Number = phone_Number;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.foodList = foodList;
        this.dateOfBirth = dateOfBirth;
        this.bookmarks = bookmarks;
        this.p_URL = p_URL;
        this.locations = locations;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getuUID() {
        return uUID;
    }

    public void setuUID(String uUID) {
        this.uUID = uUID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_Number() {
        return phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        this.phone_Number = phone_Number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<String> foodList) {
        this.foodList = foodList;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<String> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<String> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public String getP_URL() {
        return p_URL;
    }

    public void setP_URL(String p_URL) {
        this.p_URL = p_URL;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

}
