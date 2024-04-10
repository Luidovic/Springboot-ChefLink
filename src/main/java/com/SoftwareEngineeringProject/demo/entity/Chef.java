package com.SoftwareEngineeringProject.demo.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Chef")
public class Chef {

    private String usernameChef;
    private String uUID;
    private String email;
    private String phone_number;
    private String gender;
    private String firstName;
    private String lastName;
    private List<String> foodList;
    private String dateOfBirth;
    private List<String> bookmarks;
    private String p_URL;
    private List<String> locations;

    public Chef(String usernameChef, String uUID, String email, String phone_number, String gender, String firstName,
            String lastName, List<String> foodList, String dateOfBirth, List<String> bookmarks, String p_URL,
            List<String> locations) {
        this.usernameChef = usernameChef;
        this.uUID = uUID;
        this.email = email;
        this.phone_number = phone_number;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.foodList = foodList;
        this.dateOfBirth = dateOfBirth;
        this.bookmarks = bookmarks;
        this.p_URL = p_URL;
        this.locations = locations;
    }

    public String getUsernameChef() {
        return usernameChef;
    }

    public void setUsernameChef(String usernameChef) {
        this.usernameChef = usernameChef;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
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
