package com.SoftwareEngineeringProject.demo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.SoftwareEngineeringProject.demo.entity.Cart;

public interface CartDAO extends MongoRepository<Cart, String> {

}
