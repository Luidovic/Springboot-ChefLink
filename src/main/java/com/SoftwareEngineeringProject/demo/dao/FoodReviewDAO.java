package com.SoftwareEngineeringProject.demo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.SoftwareEngineeringProject.demo.entity.FoodReview;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface FoodReviewDAO extends MongoRepository<FoodReview, String> {
    
    @Query("{ 'id_food': ?0 }")
    List<FoodReview> findByFoodId(String id_food);
}
