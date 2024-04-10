package com.SoftwareEngineeringProject.demo.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        String usernameChef = req.get("usernameChef").asText();
        String uUID = req.get("uUID").asText();
        String email = req.get("email").asText();
        String phone_number = req.get("phone_number").asText();
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
                foodList.add(food.asText());
            }
        }
        // Validate attributes
        List<String> errorMessages = validateChefAttributes(uUID, usernameChef, email, phone_number, gender, firstName,
                lastName, dateOfBirth, p_URL, locations);
        if (!errorMessages.isEmpty()) {
            responseNode.put("error", "CHEF_NOT_ADDED");
            responseNode.set("info", objectMapper.valueToTree(errorMessages));
            return ResponseEntity.badRequest().body(responseNode);
        }

        Chef chef = new Chef(usernameChef, uUID, email, phone_number, gender, firstName, lastName, foodList,
                dateOfBirth,
                bookmarks, p_URL, locations);
        chefService.saveChef(chef);
        return ResponseEntity.ok(responseNode);
    }

    // Helper method to validate chef attributes
    private List<String> validateChefAttributes(String uUID, String usernameChef, String email, String phone_number,
            String gender, String firstName, String lastName, String dateOfBirth, String p_URL,
            List<String> locations) {
        List<String> errorMessages = new ArrayList<>();
        if (uUID.isEmpty())
            errorMessages.add("uUID must not be empty");
        if (checkChefExist(uUID))
            errorMessages.add("The chef already exists in the database");
        if (usernameChef.isEmpty())
            errorMessages.add("Chef's username must not be empty");
        if (firstName.isEmpty())
            errorMessages.add("First name must not be empty");
        if (lastName.isEmpty())
            errorMessages.add("Last name must not be empty");
        if (dateOfBirth.isEmpty())
            errorMessages.add("Date of birth must not be empty");
        if (phone_number.isEmpty())
            errorMessages.add("Phone number must not be empty");
        if (p_URL.isEmpty())
            errorMessages.add("Profile Picture URL must not be empty");
        if (email.isEmpty())
            errorMessages.add("Email must not be empty");
        if (gender.isEmpty())
            errorMessages.add("Gender must not be empty");
        if (locations.isEmpty())
            errorMessages.add("Locations must not be empty");
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
                    case "phone_number" -> resultNode.put(field, chef.getPhone_number());
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
                    case "usernameChef" -> resultNode.put(field, chef.getUsernameChef());
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
}
