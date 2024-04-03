package com.SoftwareEngineeringProject.demo.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.SoftwareEngineeringProject.demo.entity.Food;
import com.SoftwareEngineeringProject.demo.helperEntity.CuisineGroup;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.lang.Nullable;

@Service
public class FoodService {
    
    private FoodDAO foodRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public FoodService(FoodDAO foodRepository, MongoTemplate mongoTemplate) {
        this.foodRepository = foodRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public List<Food> getAllFood(){
        return foodRepository.findAll();
    }

    public Food addFood(Food food){
        return foodRepository.save(food);
    }

    public List<DBObject> findGroupByCategory(int amount, String groupBy){
        AggregationResults<DBObject> foodList = foodRepository.getFoodItemsGroupedByCuisine(amount, groupBy);
        return foodList.getMappedResults();
    }

    // public Food findById(String foodId) {
    //     // Implement logic to retrieve the food item by its ID from MongoDB
        
    //     List<Food> optionalFood = foodRepository.findByFoodId(foodId);
    //     System.out.println(optionalFood.toString());
    //     return new Food();
    // }

    public Food getbyIdFood(String id){
        return foodRepository.getbyIdFood(id);
    }

}
