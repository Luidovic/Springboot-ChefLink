package com.SoftwareEngineeringProject.demo.dao;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.SoftwareEngineeringProject.demo.entity.Food;
import com.SoftwareEngineeringProject.demo.helperEntity.CuisineGroup;
import com.mongodb.DBObject;
import com.mongodb.lang.Nullable;

public interface FoodDAO extends MongoRepository<Food,String> {
    @Aggregation(pipeline = {"" +
        "{$group: { _id: '$?1', collections: { $push: {" +
            "price: '$price'," +
            "timing: '$timing'," +
            "name: '$name'," +
            "total_rating: '$total_rating'," +
            "picture: '$picture'," +
            "id_food: '$id_food'," +
        "} } } }",
        "{$project: { _id: 1, collections: { $slice: ['$collections', ?0] } }}"})
    AggregationResults<DBObject> getFoodItemsGroupedByCuisine(int amount, String groupBy);
}
