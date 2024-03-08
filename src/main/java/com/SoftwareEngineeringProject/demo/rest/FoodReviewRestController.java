package com.SoftwareEngineeringProject.demo.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.SoftwareEngineeringProject.demo.dao.FoodReviewService;
import com.SoftwareEngineeringProject.demo.entity.FoodReview;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

@RestController
@RequestMapping("/api/foodreview")
public class FoodReviewRestController {
    
    private FoodReviewService foodReviewService;
    

    public FoodReviewRestController(FoodReviewService foodReviewService) {
        this.foodReviewService = foodReviewService;
    }
    
    @GetMapping("/get_by_food_id")
    public ObjectNode getFoodReview(@RequestBody JsonNode foodId) {
        List<FoodReview> result = foodReviewService.findByFoodId(foodId.get("foodId").asText());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        ObjectNode responseNode = nodeFactory.objectNode();
        ArrayNode arrayNode = objectMapper.valueToTree(result);
        responseNode.set("data", arrayNode);
        return responseNode;
    }
}
