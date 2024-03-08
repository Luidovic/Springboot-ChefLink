package com.SoftwareEngineeringProject.demo.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

@Document(collection = "Cart")
public class Cart {

    private String iD;

    private String location;
    private double price;
    private List <String> item_ids;
    private String order_date;
    private String completion_time;
    private String status;
    private String delivery_person;
    private String payment_method;
    private String user_id;



	public Cart(String location, double price, List<String> item_ids, String order_date,
            String completion_time, String status, String delivery_person, String payment_method, String user_id) {
        this.location = location;
        this.price = price;
        this.item_ids = item_ids;
        this.order_date = order_date;
        this.completion_time = completion_time;
        this.status = status;
        this.delivery_person = delivery_person;
        this.payment_method = payment_method;
        this.user_id = user_id;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getItem_ids() {
        return item_ids;
    }

    public void setItem_ids(List<String> item_ids) {
        this.item_ids = item_ids;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getCompletion_time() {
        return completion_time;
    }

    public void setCompletion_time(String completion_time) {
        this.completion_time = completion_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelivery_person() {
        return delivery_person;
    }

    public void setDelivery_person(String delivery_person) {
        this.delivery_person = delivery_person;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }


    public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
