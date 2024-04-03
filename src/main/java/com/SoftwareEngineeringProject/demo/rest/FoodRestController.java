package com.SoftwareEngineeringProject.demo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SoftwareEngineeringProject.demo.dao.FoodService;
import com.SoftwareEngineeringProject.demo.entity.Food;
import com.SoftwareEngineeringProject.demo.subEntity.FoodOption;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
//import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.DBObject;

@RestController
@RequestMapping("/api/food")
public class FoodRestController {

    @Autowired
    private FoodService foodService;

    private MongoTemplate mongotemplate;
    // @GetMapping
    // public List<Food> getAllFood(){
    // return foodService.getAllFood();
    // }

    public FoodRestController(FoodService foodService, MongoTemplate mongotemplate) {
        this.foodService = foodService;
        this.mongotemplate = mongotemplate;
    }

    @GetMapping
    public ArrayNode getAllFoodAsJson() {
        List<Food> foods = foodService.getAllFood();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        ArrayNode foodArrayNode = rootNode.putArray("foods");
        for (Food food : foods) {
            ObjectNode foodNode = objectMapper.valueToTree(food);
            foodArrayNode.add(foodNode);
        }
        return foodArrayNode;
    }

    @PostMapping("/add")
    public ResponseEntity<Food> addFood(@RequestBody Food food) {
        Food savedFood = foodService.addFood(food);
        return new ResponseEntity<>(savedFood, HttpStatus.CREATED);
    }

    @GetMapping("/get_explore_food")
    public ObjectNode getExploreFood(@RequestBody JsonNode req) {
        int amount = Integer.parseInt(req.get("amount").asText());
        String groupBy = req.get("group_by").asText();
        List<DBObject> foodList = foodService.findGroupByCategory(amount, groupBy);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        ObjectNode responseNode = nodeFactory.objectNode();
        JsonNode arrayNode = objectMapper.valueToTree(foodList);
        responseNode.set("data", arrayNode);

        return responseNode;
    }

    @GetMapping("/get_ingredients_option")
    public ResponseEntity<ObjectNode> getIngOpt(@RequestBody JsonNode req) {

        String foodId = req.get("id_food").asText();

        Criteria criteria = Criteria.where("id_food").is(foodId);
        Query query = new Query(criteria);

        Food food = mongotemplate.findOne(query, Food.class);

        if (food != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
            ObjectNode responseNode = nodeFactory.objectNode();

            responseNode.put("id_food", foodId);

            ArrayNode ingredientsArray = objectMapper.createArrayNode();
            for (String ingredient : food.getIngredients()) {
                ingredientsArray.add(ingredient);
            }
            responseNode.set("ingredients", ingredientsArray);

            ArrayNode optionsArray = objectMapper.createArrayNode();
            for (FoodOption option : food.getOptions()) {
                ObjectNode optionNode = objectMapper.valueToTree(option);
                optionsArray.add(optionNode);
            }
            responseNode.set("options", optionsArray);

            return new ResponseEntity<>(responseNode, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
