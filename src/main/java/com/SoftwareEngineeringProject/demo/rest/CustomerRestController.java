package com.SoftwareEngineeringProject.demo.rest;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
//import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

import com.SoftwareEngineeringProject.demo.dao.CustomerDAO;
import com.SoftwareEngineeringProject.demo.dao.CustomerService;
import com.SoftwareEngineeringProject.demo.entity.Customer;
import com.SoftwareEngineeringProject.demo.entity.Food;
import com.fasterxml.jackson.core.type.TypeReference;

//import jakarta.annotation.PostConstruct;

//import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.client.result.UpdateResult;
import com.fasterxml.jackson.databind.node.ArrayNode;

@RestController
@RequestMapping("/api")
public class CustomerRestController {
    private final CustomerService customerService;
    // private final CustomerDAO customerDAO;
    private final MongoTemplate mongotemplate;

    public CustomerRestController(CustomerService customerService, CustomerDAO customerDAO,
            MongoTemplate mongotemplate) {
        this.customerService = customerService;
        // this.customerDAO = customerDAO;
        this.mongotemplate = mongotemplate;
    }

    @GetMapping("/GetAllCustomers")
    public List<Customer> getCustomers() {

        return customerService.findAll();
    }

    @PutMapping("/Customer/AddC") // test cases covered
    public ResponseEntity<ObjectNode> addCustomer(@RequestBody JsonNode req) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode responseNode = objectMapper.createObjectNode();

        String username = req.get("username").asText();
        String uUID = req.get("uUID").asText();
        String email = req.get("email").asText();
        String phone_number = req.get("phone_Number").asText();
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

        // Validate attributes
        List<String> errorMessages = validateCustomerAttributes(uUID, username, email, phone_number, gender, firstName,
                lastName, dateOfBirth, p_URL);
        if (!errorMessages.isEmpty()) {
            responseNode.put("error", "CUSTOMER_NOT_ADDED");
            responseNode.set("info", objectMapper.valueToTree(errorMessages));
            return ResponseEntity.badRequest().body(responseNode);
        }

        Customer customer = new Customer(username, uUID, email, phone_number, gender, firstName, lastName, dateOfBirth,
                bookmarks, p_URL, new ArrayList<>());
        customerService.saveCustomer(customer);
        return ResponseEntity.ok(responseNode);
    }

    // Helper method to validate customer attributes
    private List<String> validateCustomerAttributes(String uUID, String username, String email, String phone_number,
            String gender, String firstName, String lastName, String dateOfBirth, String p_URL) {
        List<String> errorMessages = new ArrayList<>();
        if (uUID.isEmpty())
            errorMessages.add("uUID must not be empty");
        if (checkUserExist(uUID))
            errorMessages.add("The customer already exists in the database");
        if (username.isEmpty())
            errorMessages.add("Username must not be empty");
        if (firstName.isEmpty())
            errorMessages.add("First name must not be empty");
        if (lastName.isEmpty())
            errorMessages.add("Last name must not be empty");
        if (dateOfBirth.isEmpty())
            errorMessages.add("Date of birth must not be empty");
        if (phone_number.isEmpty())
            errorMessages.add("Phone number must not be empty");
        if (p_URL.isEmpty())
            errorMessages.add("Profile Picture must not be empty");
        if (email.isEmpty())
            errorMessages.add("Email must not be empty");
        if (gender.isEmpty())
            errorMessages.add("Gender must not be empty");
        return errorMessages;
    }

    @GetMapping("/Customer/GetAttrib")
    public ObjectNode getUserAttributes(@RequestBody JsonNode req) { // handled some of the cases not sure if there is
                                                                     // more?
        JsonNode type = req.get("id_type");
        JsonNode value = req.get("value");

        String typeS = type.asText();
        String valueS = value.asText();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();

        Criteria criteria = Criteria.where(typeS).is(valueS);
        Query query = new Query(criteria);

        Customer customer = mongotemplate.findOne(query, Customer.class);

        if (customer == null) {
            resultNode.put("error", "CUSTOMER_NOT_FOUND");
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
                    case "email" -> resultNode.put(field, customer.getemail());
                    case "uUID" -> resultNode.put(field, customer.getuUID());
                    case "firstName" -> resultNode.put(field, customer.getfirstName());
                    case "lastName" -> resultNode.put(field, customer.getlastName());
                    case "gender" -> resultNode.put(field, customer.getgender());
                    case "dateOfBirth" -> resultNode.put(field, customer.getDateOfBirth());
                    case "phone_Number" -> resultNode.put(field, customer.getphone_Number());
                    case "p_URL" -> resultNode.put(field, customer.getP_URL());
                    case "bookmarks" -> {
                        List<String> bookmarks = customer.getBookmarks();
                        ArrayNode bookmarksNode = objectMapper.createArrayNode();
                        for (String bookmark : bookmarks) {
                            bookmarksNode.add(bookmark);
                        }
                        resultNode.set(field, bookmarksNode);
                    }
                    case "locations" -> {
                        List<String> locations = customer.getLocations();
                        ArrayNode locationsNode = objectMapper.createArrayNode();
                        for (String loc : locations) {
                            locationsNode.add(loc);
                        }
                        resultNode.set(field, locationsNode);
                    }
                    case "username" -> resultNode.put(field, customer.getusername());
                }
            }
        } catch (Exception e) {

        }

        return resultNode;

    }

    @PutMapping("/addBookmark") // test cases covered
    public ResponseEntity<ObjectNode> addBookmark(@RequestBody JsonNode req) {
        String UUID = req.get("uUID").asText();
        String id_food = req.get("id_food").asText();

        boolean foodExist = checkFoodExist(id_food);
        boolean userExist = checkUserExist(UUID);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();

        if (!userExist) {
            resultNode.put("status", "fail");
            resultNode.put("description", "user not found");
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

        mongotemplate.updateFirst(query, update, Customer.class);
        return new ResponseEntity<>(resultNode, HttpStatus.ACCEPTED);
    }

    private boolean isFoodInBookmarks(String UUID, String id_food) {
        Query query = new Query(Criteria.where("uUID").is(UUID).and("bookmarks").in(id_food));
        return mongotemplate.exists(query, Customer.class);
    }

    private boolean checkFoodExist(String FoodId) {
        Query query = new Query(Criteria.where("id_food").is(FoodId));
        return mongotemplate.exists(query, Food.class);
    }

    private boolean checkUserExist(String userID) {
        Query query = new Query(Criteria.where("uUID").is(userID));
        return mongotemplate.exists(query, Customer.class);
    }

    @GetMapping("/getBookMarkedFoodId")
    private ResponseEntity<ObjectNode> getFoodIds(@RequestBody JsonNode req) {

        String uUID = req.get("uUID").asText();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();

        resultNode.put("uUID", uUID);

        Query q = new Query(Criteria.where("uUID").is(uUID));
        Customer c = mongotemplate.findOne(q, Customer.class);

        if (c == null) {
            resultNode.put("error", "CUSTOMER_NOT_FOUND");
            resultNode.put("info", "the customer is not found in the database");
            return ResponseEntity.badRequest().body(resultNode);
        }

        List<String> IdsToBeReturned = c.getBookmarks();

        ArrayNode bookmarkArr = objectMapper.createArrayNode();

        for (String s : IdsToBeReturned) {

            bookmarkArr.add(s);
        }
        resultNode.set("Bookmarks", bookmarkArr);

        return ResponseEntity.ok(resultNode);

    }

    @PutMapping("/UpdateInfo")
    private ResponseEntity<ObjectNode> updateinfo(@RequestBody JsonNode req) {

        String uUID = req.get("uUID").asText();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.put("uUID", uUID);

        Query q = new Query(Criteria.where("uUID").is(uUID));
        Customer customer = mongotemplate.findOne(q, Customer.class);

        if (customer == null) {
            resultNode.put("error", "CUSTOMER_NOT_FOUND");
            resultNode.put("info", "This customer is not in the database");
            return ResponseEntity.badRequest().body(resultNode);
        }

        List<String> AcceptedAttributes = new ArrayList<>();
        AcceptedAttributes.addAll(Arrays.asList("uUID", "username", "email", "phone_Number",
                "gender", "firstName", "lastName", "dateOfBirth", "bookmarks", "p_URL", "locations"));

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
                return ResponseEntity.badRequest().body(resultNode);
            }

        }

        for (int i = 0; i < toChange.size(); i++) {

            String attributeToChange = toChange.get(i);
            String newValue = newValues.get(i);
            // System.out.println("Attribute to change " + i + " is: " + attributeToChange);
            // System.out.println("New Value to set " + i + " is: " + newValue);
            switch (attributeToChange) {

                case "username":
                    // customer.setusername(newValue);
                    resultNode.put("username", "cannot be changed");
                    break;

                case "email":
                    // customer.setemail(newValue);
                    resultNode.put("email", "cannot be changed");
                    break;

                case "phone_Number":
                    customer.setphone_Number(newValue);
                    break;

                case "gender":
                    customer.setgender(newValue);
                    break;

                case "firstName":
                    customer.setfirstName(newValue);
                    break;

                case "lastName":
                    customer.setlastName(newValue);
                    break;

                case "dateOfBirth":
                    customer.setDateOfBirth(newValue);
                    break;

                case "p_URL":
                    customer.setp_url(newValue);
                    break;

                default:
                    break;
            }
        }
        int counter = 0;
        Update update = new Update();
        for (int i = 0; i < toChange.size(); i++) {
            String attributeToChange = toChange.get(i);
            String newValue = newValues.get(i);

            if (attributeToChange.equals("username") || attributeToChange.equals("email")) {
                continue;
            } else {
                counter++;
            }
            // System.out.println("Attribute to change now: " + attributeToChange);
            update.set(attributeToChange, newValue);
        }
        UpdateResult updateResult;
        if (counter > 0) {
            updateResult = mongotemplate.updateFirst(q, update, Customer.class);

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

    @GetMapping("/cron")
    public ResponseEntity<ObjectNode> cron() {

        return ResponseEntity.ok().body(null);
    }

}
