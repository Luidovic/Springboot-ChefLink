package com.SoftwareEngineeringProject.demo.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SoftwareEngineeringProject.demo.dao.ChefService;
import com.SoftwareEngineeringProject.demo.entity.Chef;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private boolean checkChefExist(String uUID) {
        Chef existingChef = chefService.findChefById(uUID);

        // If chef with the given UUID exists, return true
        return existingChef != null;
    }

}
