package com.SoftwareEngineeringProject.demo.rest;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

import javax.management.Query;

@RestController
@RequestMapping("/api/foodreview")
public class FoodReviewRestController {

    private FoodReviewService foodReviewService;
    private FoodService foodService;
    private MongoTemplate mongoTemplate;

    public FoodReviewRestController(FoodReviewService foodReviewService, FoodService foodService,
            MongoTemplate mongoTemplate) {
        this.foodReviewService = foodReviewService;
        this.foodService = foodService;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/get_by_food_id") // Return the comment & rating and return the username, the P_URL and the uUID +
                                   // // the whole information about the specific food.
    public ResponseEntity<ObjectNode> getFoodReview(@RequestBody JsonNode foodId) {
        // List<FoodReview> result =
        // foodReviewService.findByFoodId(foodId.get("foodId").asText());
        // ObjectMapper objectMapper = new ObjectMapper();
        // JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        // ObjectNode responseNode = nodeFactory.objectNode();
        // ArrayNode arrayNode = objectMapper.valueToTree(result);
        // responseNode.set("data", arrayNode);

        Food res = foodService.getbyIdFood(foodId.get("foodId").asText());
        System.out.println(res);
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode responseNode = objectMapper.valueToTree(res);

        List<Customer> customerList; // List of customers who commented and rated the food.

        String foodIdValue = foodId.get("foodId").asText();

        Criteria criteria = Criteria.where("id_food").is(foodIdValue);

        Query query = new Query(criteria);

        // List<FoodReview> reviews= mongoTemplate.findAll(FoodReview.class,
        // Query.query(Criteria.where("id_food").is(foodIdValue)));

        // System.out.print(reviews.toString());
        return null;
    }

    // private ObjectNode convertFoodToNode(Food food) {
    // // Implement logic to populate the ObjectNode with food details
    // // from your Food model class properties
    // ObjectNode response = new ObjectMapper().createObjectNode();
    // response.put("name", food.getName());
    // response.put("description", food.getDescription());
    // // Add other relevant properties from your Food model
    // return response;
    // }

    // @GetMapping("/getFoodReview")
    // public ResponseEntity<ObjectMapper> getTheReview(@RequestBody JsonNode
    // request){

    // String FoodIdValue= request.get("foodId").asText();

    // // Query query = new Query(Criteria.where("id_food").is(FoodIdValue));

    // return ResponseEntity.ok();
    // }

}
