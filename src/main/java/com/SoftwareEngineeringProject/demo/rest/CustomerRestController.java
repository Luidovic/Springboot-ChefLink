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

//import jakarta.annotation.PostConstruct;

//import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

@RestController
@RequestMapping("/api")
public class CustomerRestController {
    private final CustomerService customerService;
    private final CustomerDAO customerDAO;
    private final MongoTemplate mongotemplate;

    public CustomerRestController(CustomerService customerService, CustomerDAO customerDAO,
            MongoTemplate mongotemplate) {
        this.customerService = customerService;
        this.customerDAO = customerDAO;
        this.mongotemplate = mongotemplate;
    }

    // private List<Customer> cust;

    // @PostConstruct
    // public void loadData() {
    // cust = new ArrayList<>();
    // cust.add(new Customer("John1", "1", "JohnDoe1@lau.edu", "+961823495",
    // "Malee", "John1", "Doe1", "2/5/2002",
    // "@3245324"));
    // cust.add(new Customer("John2", "2", "JohnDoe2@lau.edu", "+96182395",
    // "Maleee", "John2", "Doe2", "3/5/2002",
    // "@324555"));
    // cust.add(
    // new Customer("John3", "3", "JohnDoe3@lau.edu", "+96182345", "Maleeee",
    // "John3", "Doe3", "23/56/2002",
    // "@3245324"));
    // cust.add(new Customer("John4", "4", "JohnDoe4@lau.edu", "+9618245",
    // "Maleeeee", "John4", "Doe4",
    // "23/566/2002",
    // "@32453245555"));
    // cust.add(new Customer("John5", "5", "JohnDoe5@lau.edu", "+961825", "Mal",
    // "John5", "Doe5", "23/5/20022",
    // "@32455555"));

    // }

    @GetMapping("/GetAllCustomers")
    public List<Customer> getCustomers() {

        return customerService.findAll();
    }

    // @PutMapping("/Customer/Add") // Corrected endpoint check @PutMapping
    // public ResponseEntity<String> addCustomer(@RequestBody Customer customer) {
    // System.out.println(customer.getuUID());
    // customerService.saveCustomer(customer);
    // return new ResponseEntity<>("User added successfully", HttpStatus.CREATED);
    // }

    @PutMapping("/Customer/AddToDB") // BOOKMARKS CASES ARE NOT COVERED
    public ResponseEntity<String> addCustomer(@RequestBody JsonNode req) {
        JsonNode username = req.get("username");
        JsonNode uUID = req.get("uUID");
        JsonNode email = req.get("email");
        JsonNode phone_number = req.get("phone_number");
        JsonNode gender = req.get("gender");
        JsonNode firstName = req.get("firstName");
        JsonNode lastName = req.get("lastName");
        JsonNode dateOfBirth = req.get("dateOfBirth");
        JsonNode bookmarks = req.get("bookmarks"); // Array of String
        JsonNode p_URL = req.get("p_URL");

        String usernameS = username.asText();
        String uUIDS = uUID.asText();
        String emailS = email.asText();
        String phone_numberS = phone_number.asText();
        String genderS = gender.asText();
        String firstNameS = firstName.asText();
        String lastNameS = lastName.asText();
        String dateOfBirthS = dateOfBirth.asText();
        String p_URLS = p_URL.asText();
        List<String> bookmarksList = new ArrayList<>();

        Customer customer = new Customer();

        if (uUIDS.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("uUID is a must");

        if (checkUserExist(uUIDS))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(uUIDS + " is already found in the Database");

        if (usernameS.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("username is a must");

        if (firstNameS.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("first name is a must");

        if (lastNameS.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("last name is a must");

        if (dateOfBirthS.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("date of birth is a must");

        if (phone_numberS.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("phone number is a must");

        if (p_URLS.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("p_URL is a must");

        if (emailS.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("email is a must");

        if (genderS.isEmpty())
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("gender is a must");

        customer.setuUID(uUIDS);
        customer.setfirstName(firstNameS);
        customer.setemail(emailS);
        customer.setgender(genderS);
        customer.setlastName(lastNameS);
        customer.setp_url(p_URLS);
        customer.setphone_Number(phone_numberS);
        customer.setusername(usernameS);
        customer.setDateOfBirth(dateOfBirthS);

        for (JsonNode bookmark : bookmarks)
            bookmarksList.add(bookmark.asText());

        customer.setBookmarks(bookmarksList);

        customerService.saveCustomer(customer);
        return ResponseEntity.ok("Customer saved successfully");

    }

    @GetMapping("/Customer/GetAttrib")
    public ObjectNode getUserAttributes(@RequestBody JsonNode req) {
        JsonNode type = req.get("id_type");
        JsonNode value = req.get("value");

        String typeS = type.asText();
        String valueS = value.asText();

        Criteria criteria = Criteria.where(typeS).is(valueS);
        Query query = new Query(criteria);

        Customer customer = mongotemplate.findOne(query, Customer.class);

        JsonNode attribsArray = req.get("attribs");

        List<String> need = new ArrayList<>();

        for (JsonNode attrib : attribsArray) {
            String attribute = attrib.asText();
            need.add(attribute);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();
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
                    case "username" -> resultNode.put(field, customer.getusername());
                }
            }
        } catch (Exception e) {

        }

        return resultNode;

    }

    @PutMapping("/addBookmark")
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

    @GetMapping("/getFoodId")
    private ResponseEntity<ObjectNode> getFoodIds(@RequestBody JsonNode req) {

        String uUID = req.get("uUID").asText();

        boolean userStatus = checkUserExist(uUID);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();

        if (userStatus) {
            resultNode.put("uUID", uUID);

            Query q = new Query(Criteria.where("uUID").is(uUID));
            Customer c = mongotemplate.findOne(q, Customer.class);
            List<String> IdsToBeReturned = c.getBookmarks();
            // System.out.print(c);
            ArrayNode bookmarkArr = objectMapper.createArrayNode();

            for (String s : IdsToBeReturned) {
                // System.out.println(s);
                bookmarkArr.add(s);
            }
            resultNode.set("Bookmarks", bookmarkArr);

        }
        return ResponseEntity.ok(resultNode);

    }

}
