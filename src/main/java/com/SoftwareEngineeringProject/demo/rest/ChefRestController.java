package com.SoftwareEngineeringProject.demo.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SoftwareEngineeringProject.demo.dao.ChefService;
import com.SoftwareEngineeringProject.demo.entity.Chef;
import com.SoftwareEngineeringProject.demo.entity.Food;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

@RestController
@RequestMapping("/api")
public class ChefRestController {

    private MongoTemplate mongotemplate;
    private ChefService chefService;

    public ChefRestController(MongoTemplate mongotemplate, ChefService chefService) {
        this.mongotemplate = mongotemplate;
        this.chefService = chefService;
    }

    @GetMapping("/GetAllChefs")
    public List<Chef> getChefs() {
        return chefService.getAll();
    }

    @PostMapping("/AddChef")
    public ResponseEntity<ObjectNode> addChef(@RequestBody JsonNode req) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode responseNode = objectMapper.createObjectNode();

        String username = req.get("username").asText();
        String uUID = req.get("uUID").asText();
        String email = req.get("email").asText();
        String phone_Number = req.get("phone_Number").asText();
        String gender = req.get("gender").asText();
        String firstName = req.get("firstName").asText();
        String lastName = req.get("lastName").asText();
        String dateOfBirth = req.get("dateOfBirth").asText();
        List<String> bookmarks = new ArrayList<>();
        JsonNode bookmarksNode = req.get("bookmarks");
        if (bookmarksNode.isArray()) {
            for (JsonNode bookmark : bookmarksNode) {
                bookmarks.add(bookmark.asText());
            }
        }
        String p_URL = req.get("p_URL").asText();
        List<String> locations = new ArrayList<>();
        JsonNode locationsNode = req.get("locations");
        if (locationsNode.isArray()) {
            for (JsonNode location : locationsNode) {
                locations.add(location.asText());
            }
        }

        List<String> foodList = new ArrayList<>();
        JsonNode foodListNode = req.get("foodList");
        if (foodListNode.isArray()) {
            for (JsonNode food : foodListNode) {
                String foodId = food.asText();
                if (!checkFoodExist(foodId)) {
                    responseNode.put("error", "CHEF_NOT_ADDED");
                    responseNode.put("info", "Food with id " + foodId + " does not exist");
                    return ResponseEntity.badRequest().body(responseNode);
                }
                foodList.add(foodId);
            }
        }
        // Validate attributes
        List<String> errorMessages = validateChefAttributes(uUID, username, email, phone_Number, gender, firstName,
                lastName, dateOfBirth, p_URL, locations);
        if (!errorMessages.isEmpty()) {
            responseNode.put("error", "CHEF_NOT_ADDED");
            responseNode.set("info", objectMapper.valueToTree(errorMessages));
            return ResponseEntity.badRequest().body(responseNode);
        }

        Chef chef = new Chef(username, uUID, email, phone_Number, gender, firstName, lastName, foodList,
                dateOfBirth,
                bookmarks, p_URL, locations);
        chefService.saveChef(chef);
        return ResponseEntity.ok(responseNode);
    }

    // Helper method to validate chef attributes
    private List<String> validateChefAttributes(String uUID, String username, String email, String phone_Number,
            String gender, String firstName, String lastName, String dateOfBirth, String p_URL,
            List<String> locations) {
        List<String> errorMessages = new ArrayList<>();
        if (uUID.isEmpty())
            errorMessages.add("uUID must not be empty");
        if (checkChefExist(uUID))
            errorMessages.add("The chef already exists in the database");
        if (username.isEmpty())
            errorMessages.add("Chef's username must not be empty");
        if (firstName.isEmpty())
            errorMessages.add("First name must not be empty");
        if (lastName.isEmpty())
            errorMessages.add("Last name must not be empty");
        if (dateOfBirth.isEmpty())
            errorMessages.add("Date of birth must not be empty");
        if (phone_Number.isEmpty())
            errorMessages.add("Phone number must not be empty");
        if (p_URL.isEmpty())
            errorMessages.add("Profile Picture URL must not be empty");
        if (email.isEmpty())
            errorMessages.add("Email must not be empty");
        if (gender.isEmpty())
            errorMessages.add("Gender must not be empty");
        // if (locations.isEmpty())
        // errorMessages.add("Locations must not be empty");
        return errorMessages;
    }

    // Method to check if the chef already exists
    // private boolean checkChefExist(String uUID) {
    // Chef existingChef = chefService.findChefById(uUID);

    // // If chef with the given UUID exists, return true
    // return existingChef != null;
    // }

    @GetMapping("/Chef/GetAttrib")
    public ObjectNode getChefAttributes(@RequestBody JsonNode req) {

        JsonNode type = req.get("id_type");
        JsonNode value = req.get("value");

        String typeS = type.asText();
        String valueS = value.asText();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();

        Criteria criteria = Criteria.where(typeS).is(valueS);
        Query query = new Query(criteria);

        Chef chef = mongotemplate.findOne(query, Chef.class);

        if (chef == null) {
            resultNode.put("error", "CHEF_NOT_FOUND");
            return resultNode;
        }

        JsonNode attribsArray = req.get("attribs");

        List<String> need = new ArrayList<>();

        for (JsonNode attrib : attribsArray) {
            String attribute = attrib.asText();
            need.add(attribute);
        }

        try {
            for (int i = 0; i < need.size(); i++) {
                String field = need.get(i);

                switch (field) {
                    case "email" -> resultNode.put(field, chef.getEmail());
                    case "uUID" -> resultNode.put(field, chef.getuUID());
                    case "firstName" -> resultNode.put(field, chef.getFirstName());
                    case "lastName" -> resultNode.put(field, chef.getLastName());
                    case "gender" -> resultNode.put(field, chef.getGender());
                    case "dateOfBirth" -> resultNode.put(field, chef.getDateOfBirth());
                    case "phone_Number" -> resultNode.put(field, chef.getPhone_Number());
                    case "p_URL" -> resultNode.put(field, chef.getP_URL());
                    case "bookmarks" -> {
                        List<String> bookmarks = chef.getBookmarks();
                        ArrayNode bookmarksNode = objectMapper.createArrayNode();
                        for (String bookmark : bookmarks) {
                            bookmarksNode.add(bookmark);
                        }
                        resultNode.set(field, bookmarksNode);
                    }
                    case "locations" -> {
                        List<String> locations = chef.getLocations();
                        ArrayNode locationsNode = objectMapper.createArrayNode();
                        for (String loc : locations) {
                            locationsNode.add(loc);
                        }
                        resultNode.set(field, locationsNode);
                    }
                    case "username" -> resultNode.put(field, chef.getUsername());
                }
            }
        } catch (Exception e) {

        }

        return resultNode;

    }

    @PutMapping("/addBookmarkChef") // test cases covered
    public ResponseEntity<ObjectNode> addBookmark(@RequestBody JsonNode req) {
        String UUID = req.get("uUID").asText();
        String id_food = req.get("id_food").asText();

        boolean foodExist = checkFoodExist(id_food);
        boolean chefExist = checkChefExist(UUID);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();

        if (!chefExist) {
            resultNode.put("status", "fail");
            resultNode.put("description", "chef not found");
            return new ResponseEntity<>(resultNode, HttpStatus.NOT_FOUND);
        }

        if (!foodExist) {
            resultNode.put("status", "fail");
            resultNode.put("description", "food not found");
            return new ResponseEntity<>(resultNode, HttpStatus.NOT_FOUND);
        }

        Query query = new Query(Criteria.where("uUID").is(UUID));
        Update update = new Update();

        if (isFoodInBookmarks(UUID, id_food)) {
            update.pull("bookmarks", id_food);
            resultNode.put("status", "successful");
            resultNode.put("bookmark", "unbookmarked");
        } else {
            update.addToSet("bookmarks", id_food);
            resultNode.put("status", "successful");
            resultNode.put("bookmark", "bookmarked");
        }

        mongotemplate.updateFirst(query, update, Chef.class);
        return new ResponseEntity<>(resultNode, HttpStatus.ACCEPTED);
    }

    private boolean isFoodInBookmarks(String UUID, String id_food) {
        Query query = new Query(Criteria.where("uUID").is(UUID).and("bookmarks").in(id_food));
        return mongotemplate.exists(query, Chef.class);
    }

    private boolean checkFoodExist(String FoodId) {
        Query query = new Query(Criteria.where("id_food").is(FoodId));
        return mongotemplate.exists(query, Food.class);
    }

    private boolean checkChefExist(String chefID) {
        Query query = new Query(Criteria.where("uUID").is(chefID));
        return mongotemplate.exists(query, Chef.class);
    }

    @GetMapping("/getBookMarkedFoodIdChef")
    private ResponseEntity<ObjectNode> getFoodIds(@RequestBody JsonNode req) {

        String uUID = req.get("uUID").asText();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("uUID", uUID);

        Query q = new Query(Criteria.where("uUID").is(uUID));
        Chef chef = mongotemplate.findOne(q, Chef.class);

        if (chef == null) {
            resultNode.put("error", "CHEF_NOT_FOUND");
            resultNode.put("info", "the chef is not found in the database");
            return ResponseEntity.badRequest().body(resultNode);
        }

        List<String> IdsToBeReturned = chef.getBookmarks();

        ArrayNode bookmarkArr = objectMapper.createArrayNode();

        for (String s : IdsToBeReturned) {
            bookmarkArr.add(s);
        }
        resultNode.set("Bookmarks", bookmarkArr);

        return ResponseEntity.ok(resultNode);

    }

    @DeleteMapping("/DeleteChef")
    private ResponseEntity<ObjectNode> DeleteChef(@RequestBody JsonNode req) {

        String uUIDs = req.get("uUID").asText();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode responseNode = objectMapper.createObjectNode();

        if (!checkChefExist(uUIDs)) {
            responseNode.put("error", "CHEF_NOT_FOUND");
            return ResponseEntity.badRequest().body(responseNode);
        }

        Query query = new Query(Criteria.where("uUID").is(uUIDs));
        DeleteResult deleteResult = mongotemplate.remove(query, Chef.class);
        if (deleteResult.wasAcknowledged() && deleteResult.getDeletedCount() > 0) {
            responseNode.put("success", "CHEF_DELETED");
            return ResponseEntity.ok().body(responseNode);
        } else {
            responseNode.put("error", "CHEF_DELETION_FAILED");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseNode);
        }

    }

    @PutMapping("/UpdateInfoChef")
    private ResponseEntity<ObjectNode> updateInfo(@RequestBody JsonNode req) {

        String uUID = req.get("uUID").asText();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("uUID", uUID);

        Query q = new Query(Criteria.where("uUID").is(uUID));
        Chef chef = mongotemplate.findOne(q, Chef.class);

        if (chef == null) {
            resultNode.put("error", "CHEF_NOT_FOUND");
            resultNode.put("info", "This chef is not in the database");
            return ResponseEntity.badRequest().body(resultNode);
        }
        List<String> AcceptedAttributes = new ArrayList<>();

        AcceptedAttributes.addAll(Arrays.asList("username", "uUID", "email", "phone_Number", "gender", "firstName",
                "lastName", "foodList", "dateOfBirth", "bookmarks", "p_URL", "locations"));

        JsonNode toChangeNode = req.get("To Change");
        JsonNode newValuesNode = req.get("New Values");
        List<String> toChange = new ArrayList<>();
        List<String> newValues = new ArrayList<>();

        for (JsonNode node : toChangeNode) {
            toChange.add(node.asText());
        }
        for (JsonNode node : newValuesNode) {
            newValues.add(node.asText());
        }

        if (toChange.size() != newValues.size()) {
            resultNode.put("error", "LIST_LENGTH_MISMATCH");
            return ResponseEntity.badRequest().body(resultNode);
        }

        for (int i = 0; i < toChange.size(); i++) {
            String attribute = toChange.get(i);

            if (!AcceptedAttributes.contains(attribute)) {
                resultNode.put("error", "ATTRIBUTE_NOT_FOUND");
                resultNode.put("info", attribute + " is not present in the Database");
            }

        }

        for (int i = 0; i < toChange.size(); i++) {
            String attributeToChange = toChange.get(i);
            String newValue = newValues.get(i);

            switch (attributeToChange) {
                case "username":
                    resultNode.put("username", "cannot be changed");
                    break;

                case "uUID":
                    resultNode.put("uUID", "cannot be changed");
                    break;

                case "email":
                    resultNode.put("email", "cannot be changed");
                    break;

                case "phone_Number":
                    chef.setPhone_Number(newValue);
                    break;

                case "gender":
                    chef.setGender(newValue);
                    break;

                case "firstName":
                    chef.setFirstName(newValue);
                    break;

                case "lastName":
                    chef.setLastName(newValue);
                    break;

                case "dateOfBirth":
                    chef.setDateOfBirth(newValue);
                    break;

                case "p_URL":
                    chef.setP_URL(newValue);
                    break;

                // case "foodList":
                // JsonNode foodListToUpdateNode = newValuesNode.get("foodList");
                // List<String> foodIdsToUpdate = new ArrayList<>();
                // for (JsonNode foodIdNode : foodListToUpdateNode) {
                // foodIdsToUpdate.add(foodIdNode.asText());
                // }

                // List<Food> foodList = mongotemplate.findAll(Food.class);
                // List<String> foodIds = new ArrayList<>();
                // for (Food food : foodList) {
                // foodIds.add(food.getId_food());
                // }

                // List<String> invalidFoodIds = new ArrayList<>();
                // for (String foodId : foodIdsToUpdate) {
                // if (!foodIds.contains(foodId)) {
                // invalidFoodIds.add(foodId);
                // }
                // }

                // if (!invalidFoodIds.isEmpty()) {
                // resultNode.put("error", "INVALID_FOOD_IDS");
                // resultNode.put("info", "The following food IDs are not valid: " +
                // invalidFoodIds);
                // return ResponseEntity.badRequest().body(resultNode);
                // }

                // chef.getFoodList().addAll(foodIdsToUpdate);
                // break;

                // case "locations":
                // List<String> existingLocations = chef.getLocations();
                // existingLocations.addAll(newValues); // Assuming newValues contains the new
                // locations
                // chef.setLocations(existingLocations);
                // break;

                default:
                    break;
            }
        }
        int counter = 0;

        Update update = new Update();
        for (int i = 0; i < toChange.size(); i++) {
            String attributeToChange = toChange.get(i);
            String newValue = newValues.get(i);

            if (attributeToChange.equals("username") || attributeToChange.equals("email")
                    || attributeToChange.equals("uUID")) {
                continue;
            } else {
                counter++;
            }
            update.set(attributeToChange, newValue);
        }
        UpdateResult updateResult;
        if (counter > 0) {
            updateResult = mongotemplate.updateFirst(q, update, Chef.class);

            if (updateResult.getModifiedCount() > 0) {
                resultNode.put("success", "ATTRIBUTES_UPDATED");
                return ResponseEntity.ok().body(resultNode);
            } else {
                resultNode.put("error", "NO_ATTRIBUTES_UPDATED");
                return ResponseEntity.badRequest().body(resultNode);
            }

        } else {
            resultNode.put("error", "CANNOT_BE_UPDATED");
            resultNode.put("info", "you only provided email and username");
            return ResponseEntity.badRequest().body(resultNode);
        }

    }

    @PutMapping("/UpdateFoodList")
    private ResponseEntity<ObjectNode> updateFoodList(@RequestBody JsonNode req) {

        String uUIDs = req.get("uUID").asText();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();

        if (!checkChefExist(uUIDs)) {
            resultNode.put("error", "CHEF_NOT_FOUND");
            return ResponseEntity.badRequest().body(resultNode);
        }

        JsonNode foodList = req.get("foodList");
        List<String> FoodListID = new ArrayList<>();

        for (JsonNode node : foodList) {
            FoodListID.add(node.asText());
        }

        for (int i = 0; i < FoodListID.size(); i++) {
            String current = FoodListID.get(i);
            if (!checkFoodExist(current)) {
                resultNode.put("error", "FOOD_NOT_FOUND");
                return ResponseEntity.badRequest().body(resultNode);
            }
        }
        Query q = new Query(Criteria.where("uUID").is(uUIDs));
        Chef chef = mongotemplate.findOne(q, Chef.class);

        if (chef == null) {
            resultNode.put("error", "CHEF_NOT_FOUND");
            return ResponseEntity.badRequest().body(resultNode);
        }
        Update update = new Update();
    
        update.set("foodList", FoodListID);
    
        // Perform the update operation
        UpdateResult updateResult = mongotemplate.updateFirst(q, update, Chef.class);
    
        if (updateResult.getModifiedCount() > 0) {
            resultNode.put("success", "FOOD_LIST_UPDATED");
            return ResponseEntity.ok().body(resultNode);
        } else {
            resultNode.put("error", "FOOD_LIST_NOT_UPDATED");
            return ResponseEntity.badRequest().body(resultNode);
        }
    }

    @PutMapping("/UpdateChefLocation")
    private ResponseEntity<ObjectNode> updateChefLocation(@RequestBody JsonNode req){
        
        String uUIDs = req.get("uUID").asText();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();

        if (!checkChefExist(uUIDs)) {
            resultNode.put("error", "CHEF_NOT_FOUND");
            return ResponseEntity.badRequest().body(resultNode);
        }

        JsonNode LocationNode = req.get("Locations");
        List<String> Locations = new ArrayList<>();

        for (JsonNode node : LocationNode) {
            Locations.add(node.asText());
        }

        Query q = new Query(Criteria.where("uUID").is(uUIDs));
        Chef chef = mongotemplate.findOne(q, Chef.class);

        if (chef == null) {
            resultNode.put("error", "CHEF_NOT_FOUND");
            return ResponseEntity.badRequest().body(resultNode);
        }
        Update update = new Update();
        update.set("locations", Locations);
    
        // Perform the update operation
        UpdateResult updateResult = mongotemplate.updateFirst(q, update, Chef.class);
    
        if (updateResult.getModifiedCount() > 0) {
            resultNode.put("success", "LOCATION_LIST_UPDATED");
            return ResponseEntity.ok().body(resultNode);
        } else {
            resultNode.put("error", "LOCATION_LIST_NOT_UPDATED");
            return ResponseEntity.badRequest().body(resultNode);
        }
    }

    

}
