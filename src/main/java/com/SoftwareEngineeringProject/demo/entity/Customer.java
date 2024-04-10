package com.SoftwareEngineeringProject.demo.entity;

import java.util.List;

//import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "Customer")
public class Customer {

    private String uUID;

    private String username;

    private String email;

    private String phone_Number;

    private String gender;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private List<String> bookmarks;
    
    private String p_URL;

    private List<String> locations;
    
    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getuUID() {
        return uUID;
    }

    public void setuUID(String uUID) {
        this.uUID = uUID;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getphone_Number() {
        return phone_Number;
    }
    
    public void setphone_Number(String phone_Number) {
        this.phone_Number = phone_Number;
    }

    public String getgender() {
        return gender;
    }

    public void setgender(String gender) {
        this.gender = gender;
    }
    
    public String getfirstName() {
        return firstName;
    }

    public void setfirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getlastName() {
        return lastName;
    }
    
    public void setlastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(String d) {
        this.dateOfBirth = d;
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

    public void setp_url(String p) {
        this.p_URL = p;
    }

    public Customer(String username, String uUID, String email, String phone_Number, String gender, String firstName,
            String lastName, String dateOfBirth, List<String> bookmarks, String p_URL, List<String> locations) {
        this.username = username;
        this.uUID = uUID;
        this.email = email;
        this.phone_Number = phone_Number;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.bookmarks = bookmarks;
        this.p_URL = p_URL;
        this.locations = locations;
    }

    public Customer() {

    }

    @Override
    public String toString() {
        return "Customer{ " +
                "uUID= " + uUID +
                ",First Name " + firstName +
                ", Last Name " + lastName +
                ", email= " + email+
                ", Bookmark= " +bookmarks.get(0);
    }



}
