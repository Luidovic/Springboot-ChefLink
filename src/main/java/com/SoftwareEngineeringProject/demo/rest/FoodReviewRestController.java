package com.SoftwareEngineeringProject.demo.rest;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.SoftwareEngineeringProject.demo.dao.CustomerService;
import com.SoftwareEngineeringProject.demo.dao.FoodReviewService;
import com.SoftwareEngineeringProject.demo.dao.FoodService;
import com.SoftwareEngineeringProject.demo.entity.Customer;
import com.SoftwareEngineeringProject.demo.entity.Food;
import com.SoftwareEngineeringProject.demo.entity.FoodReview;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.client.MongoCollection;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

@RestController
@RequestMapping("/api/foodreview")
public class FoodReviewRestController {

    private FoodReviewService foodReviewService;
    private FoodService foodService;
    private MongoTemplate mongoTemplate;
    private CustomerService customerService;

    public FoodReviewRestController(FoodReviewService foodReviewService, FoodService foodService,
            MongoTemplate mongoTemplate, CustomerService customerService) {
        this.foodReviewService = foodReviewService;
        this.foodService = foodService;
        this.mongoTemplate = mongoTemplate;
        this.customerService = customerService;
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
            ObjectNode customerNode = objectMapper.createObjectNode();
            Customer customer = customerService.findCustomerById(f.getID_User());

            if (customer == null) {
                customerNode.put("error", "CUSTOMER_NOT_FOUND");
                customerNode.put("info", "The customer is not present in the database");
                return ResponseEntity.badRequest().body(customerNode);
            }

            customerNode.put("customer ID", f.getID_User());
            customerNode.put("username", customer.getusername());
            customerNode.put("Profile Picture", customer.getP_URL());
            customerNode.put("comment", f.getComment());
            customerNode.put("rating", f.getRating());
            customersNode.add(customerNode);
        }
        responseNode.set("customers", customersNode);
        return ResponseEntity.ok(responseNode);
    }
}
