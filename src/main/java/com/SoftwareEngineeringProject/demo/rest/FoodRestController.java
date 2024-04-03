package com.SoftwareEngineeringProject.demo.rest;

import java.util.ArrayList;
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

    @GetMapping("/GetAllFood")
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

    @PostMapping("/add") // errors handled mostly
    public ResponseEntity<ObjectNode> addFood(@RequestBody Food food) {

        List<String> errors = new ArrayList<>();

        if (food.getId_food() == null || food.getId_food().isEmpty()) {
            errors.add("ID Food must not be empty");
        }
        if (food.getName() == null || food.getName().isEmpty()) {
            errors.add("Name must not be empty");
        }
        if (food.getPrice() < 0) {
            errors.add("Price must be non-negative");
        }
        if (food.getPicture() == null || food.getPicture().isEmpty()) {
            errors.add("Picture must not be empty");
        }
        if (food.getIngredients() == null || food.getIngredients().isEmpty()) {
            errors.add("Ingredients must not be empty");
        }
        if (food.getDescription() == null || food.getDescription().isEmpty()) {
            errors.add("Description must not be empty");
        }
        if (food.getTiming() == null || food.getTiming().isEmpty()) {
            errors.add("Timing must not be empty");
        }
        if (food.getOptions() == null || food.getOptions().isEmpty()) {
            errors.add("Options must not be empty");
        } else {
            for (FoodOption option : food.getOptions()) {
                if (option.getOption_name() == null || option.getOption_name().isEmpty()) {
                    errors.add("Option name must not be empty");
                }
                if (option.getOption_price() < 0) {
                    errors.add("Option price must be non-negative");
                }
                if (option.getOption_type() == null) {
                    errors.add("Option type must not be null");
                }
            }
        }
        if (food.getCategory() == null) {
            errors.add("Category must not be null");
        } else {
            if (food.getCategory().getCuisine() == null || food.getCategory().getCuisine().isEmpty()) {
                errors.add("Cuisine must not be empty");
            }
            if (food.getCategory().getType() == null || food.getCategory().getType().isEmpty()) {
                errors.add("Type must not be empty");
            }
            if (food.getCategory().getPerks() == null) {
                errors.add("Perks must not be null");
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();

        if (!errors.isEmpty()) {
            resultNode.put("error", "FOOD_NOT_ADDED");
            resultNode.set("info", objectMapper.valueToTree(errors));
            return ResponseEntity.badRequest().body(resultNode);
        }

        if (checkFoodExist(food.getId_food())) {

            resultNode.put("error", "FOOD_NOT_ADDED");
            resultNode.put("info", "This food is already present in the database");
            return ResponseEntity.badRequest().body(resultNode);

        }
        Food savedFood = foodService.addFood(food);

        resultNode.put("status", "saved");
        return ResponseEntity.ok(resultNode);
    }

    private boolean checkFoodExist(String FoodId) {
        Query query = new Query(Criteria.where("id_food").is(FoodId));
        return mongotemplate.exists(query, Food.class);
    }

    @GetMapping("/get_explore_food") // errors need to be handled.
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
