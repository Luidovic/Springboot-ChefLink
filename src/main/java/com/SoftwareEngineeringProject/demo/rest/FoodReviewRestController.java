package com.SoftwareEngineeringProject.demo.rest;

import org.apache.tomcat.util.json.JSONFilter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.SoftwareEngineeringProject.demo.dao.ChefService;
import com.SoftwareEngineeringProject.demo.dao.CustomerService;
import com.SoftwareEngineeringProject.demo.dao.FoodReviewService;
import com.SoftwareEngineeringProject.demo.dao.FoodService;
import com.SoftwareEngineeringProject.demo.entity.Chef;
import com.SoftwareEngineeringProject.demo.entity.Customer;
import com.SoftwareEngineeringProject.demo.entity.Food;
import com.SoftwareEngineeringProject.demo.entity.FoodReview;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.client.MongoCollection;

import ch.qos.logback.core.net.SyslogOutputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@RestController
@RequestMapping("/api/foodreview")
public class FoodReviewRestController {

    private FoodReviewService foodReviewService;
    private FoodService foodService;
    private MongoTemplate mongoTemplate;
    private CustomerService customerService;
    private ChefService chefService;

    public FoodReviewRestController(FoodReviewService foodReviewService, FoodService foodService,
            MongoTemplate mongoTemplate, CustomerService customerService, ChefService chefService) {
        this.foodReviewService = foodReviewService;
        this.foodService = foodService;
        this.mongoTemplate = mongoTemplate;
        this.customerService = customerService;
        this.chefService = chefService;
    }

    @GetMapping("/get_by_food_id") // Tested the major Cases
    public ResponseEntity<ObjectNode> getFoodReview(@RequestBody JsonNode foodId) {

        Food res = foodService.getbyIdFood(foodId.get("foodId").asText());

        if (res == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode responseNode = objectMapper.createObjectNode();
            responseNode.put("error", "FOOD_NOT_FOUND");
            responseNode.put("info", "The given food ID is not present in the Database Or Does not have a review");
            return ResponseEntity.badRequest().body(responseNode);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode responseNode = objectMapper.valueToTree(res);

        String foodIdValue = foodId.get("foodId").asText();
        Criteria criteria = Criteria.where("id_food").is(foodIdValue);
        Query query = new Query(criteria);

        List<FoodReview> foodReviews = mongoTemplate.find(query, FoodReview.class);

        ArrayNode customersNode = objectMapper.createArrayNode();

        for (FoodReview f : foodReviews) {
            String username = "";
            ObjectNode customerNode = objectMapper.createObjectNode();
            Customer customer = customerService.findCustomerById(f.getID_User());

            if (customer == null) {
                Chef chef = chefService.findChefById(f.getID_User());
                if (chef == null) {
                    customerNode.put("error", "USER_NOT_FOUND");
                    customerNode.put("info", "The user is not present in the database");
                    return ResponseEntity.badRequest().body(customerNode);
                }
                username = chef.getUsername();
            } else {
                username = customer.getusername();
            }
            

            customerNode.put("customer ID", f.getID_User());
            customerNode.put("username", username);
            customerNode.put("comment", f.getComment());
            customerNode.put("rating", f.getRating());
            customersNode.add(customerNode);
        }
        responseNode.set("customers", customersNode);
        return ResponseEntity.ok(responseNode);
    }

    @PostMapping("/Addreview")
    public ResponseEntity<ObjectNode> AddReview(@RequestBody JsonNode req) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();

        String user = req.get("id_user").asText();
        String food = req.get("id_food").asText();
        String commentS = req.get("comment").asText();
        // String rating= req.get("rating").asText();
        JsonNode ratingNode = req.get("rating");

        if (!checkuser(user)) {
            resultNode.put("error", "USER_NOT_FOUND");
        }
        if (!checkfood(food)) {
            resultNode.put("error", "FOOD_NOT_FOUND");
        }
        if (ratingNode == null || !ratingNode.isDouble()) {
            resultNode.put("error", "RATING_NOT_DOUBLE");
        }

        if(ratingNode.asDouble()>5){
            resultNode.put("error", "RATING_ABOVE_5");
        }

        if (!checkuser(user) || !checkfood(food) || ratingNode == null || !ratingNode.isDouble()||ratingNode.asDouble()>5) {
            return ResponseEntity.badRequest().body(resultNode);
        }
        // now the user exists and the food exists and the rating is a double.
        updateTotalRating(food, ratingNode.asDouble());

        double rating = ratingNode.asDouble();

        // Create a new FoodReview instance
        FoodReview review = new FoodReview();
        review.setId_food(food);
        review.setID_User(user);
        review.setRating(rating);
        review.setComment(commentS);

        foodReviewService.saveReview(review);
        resultNode.put("success", "REVIEW_ADDED");
        return ResponseEntity.ok().body(resultNode);
    }

    private boolean checkuser(String user) {
        Query q = new Query(Criteria.where("uUID").is(user));
        return mongoTemplate.exists(q, Customer.class);
    }

    private boolean checkchef(String user) {
        Query q = new Query(Criteria.where("uUID").is(user));
        return mongoTemplate.exists(q, Chef.class);
    }

    private boolean checkfood(String food) {
        Query q = new Query(Criteria.where("id_food").is(food));
        return mongoTemplate.exists(q, Food.class);
    }

    private void updateTotalRating(String foodId, double newRating) {
        // Retrieve the food item
        Query q = new Query(Criteria.where("id_food").is(foodId));
        Food food = mongoTemplate.findOne(q, Food.class);

        // Update total_rating
        double currentTotalRating = food.getTotal_rating();
        int totalRatingsCount = food.getRatings_count();

        // Calculate new total_rating based on new and existing ratings
        double newTotalRating = ((currentTotalRating * totalRatingsCount) + newRating) / (totalRatingsCount + 1);

        // Ensure new total_rating is within the range of 0 to 5
        newTotalRating = Math.max(0, Math.min(5, newTotalRating));

        // Update food document with new total_rating and increment ratings_count
        Update update = new Update()
                .set("total_rating", newTotalRating)
                .inc("ratings_count", 1); // Increment ratings_count

        mongoTemplate.updateFirst(q, update, Food.class);
    }

    @PostMapping("/AddreviewChef")
    public ResponseEntity<ObjectNode> AddreviewChef(@RequestBody JsonNode req) {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();

        String user = req.get("id_user").asText();
        String food = req.get("id_food").asText();
        String commentS = req.get("comment").asText();
        // String rating= req.get("rating").asText();
        JsonNode ratingNode = req.get("rating");

        if (!checkchef(user)) {
            resultNode.put("error", "CHEF_NOT_FOUND");
        }
        if (!checkfood(food)) {
            resultNode.put("error", "FOOD_NOT_FOUND");
        }
        if (ratingNode == null || !ratingNode.isDouble()) {
            resultNode.put("error", "RATING_NOT_DOUBLE");
        }

        if(ratingNode.asDouble()>5){
            resultNode.put("error", "RATING_ABOVE_5");
        }

        if (!checkchef(user) || !checkfood(food) || ratingNode == null || !ratingNode.isDouble()||ratingNode.asDouble()>5) {
            return ResponseEntity.badRequest().body(resultNode);
        }
        // now the user exists and the food exists and the rating is a double.
        updateTotalRating(food, ratingNode.asDouble());

        double rating = ratingNode.asDouble();

        // Create a new FoodReview instance
        FoodReview review = new FoodReview();
        review.setId_food(food);
        review.setID_User(user);
        review.setRating(rating);
        review.setComment(commentS);

        foodReviewService.saveReview(review);
        resultNode.put("success", "REVIEW_ADDED");
        return ResponseEntity.ok().body(resultNode);
    }
}
