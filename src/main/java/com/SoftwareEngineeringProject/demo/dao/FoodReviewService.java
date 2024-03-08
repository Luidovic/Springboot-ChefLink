package com.SoftwareEngineeringProject.demo.dao;

import org.springframework.stereotype.Service;
import java.util.*;
import com.SoftwareEngineeringProject.demo.entity.FoodReview;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class FoodReviewService{
    private FoodReviewDAO foodReviewRepository;

    public FoodReviewService(FoodReviewDAO foodReviewRepository) {
        this.foodReviewRepository = foodReviewRepository;
    }

    public List<FoodReview> findByFoodId(String foodId){
        List<FoodReview> foodReviews = foodReviewRepository.findByFoodId(foodId);
        return foodReviews;
    }

    

}
