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

    @GetMapping("/Customer")
    public List<Customer> getCustomers() {

        return customerService.findAll();
    }

    @PutMapping("/Customer/Add") // Corrected endpoint check @PutMapping
    public ResponseEntity<String> addCustomer(@RequestBody Customer customer) {
        System.out.println(customer.getuUID());
        customerService.saveCustomer(customer);
        return new ResponseEntity<>("User added successfully", HttpStatus.CREATED);
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

    // @PutMapping("/addBookmark")
    // public ResponseEntity<String> addBookmark(@RequestBody Map<String, String> request) {
    //     String userId = request.get("userId");
    //     String foodId = request.get("foodId");
    //     customerService.addBookmark(userId, foodId);
    //     return ResponseEntity.ok("Bookmark added successfully");
    // }
    @PutMapping("/addBookmark")
    public ResponseEntity<String> addBookmark(@RequestBody JsonNode req){
        String UUID= req.get("uUID").asText();
        String id_food= req.get("id_food").asText();

        boolean foodExist=checkFoodExist(id_food);
        boolean userExist=checkUserExist(UUID);

        if(foodExist && userExist){
            updateCustomerBookmark(UUID, id_food);
            return new ResponseEntity<>("Bookmark added successfully", HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        }

    private boolean checkFoodExist(String FoodId){
        Query q=new Query(Criteria.where("id_food").is(FoodId));
        long count = mongotemplate.count(q, Food.class);
        return count > 0;
    }

    private boolean checkUserExist(String userID){
        Query q=new Query(Criteria.where("uUID").is(userID));
        long count=mongotemplate.count(q,Customer.class);
        return count>0;
    }
        
    private void updateCustomerBookmark(String uUID, String id_food){
        Query q=new Query(Criteria.where("uUID").is(uUID));
        Update update=new Update().addToSet("bookmarks",id_food );
        mongotemplate.updateFirst(q, update, Customer.class);
    }

   
    @GetMapping("/getFoodId")
    private ResponseEntity<ObjectNode> getFoodIds(@RequestBody JsonNode req){
        
        String uUID= req.get("uUID").asText();

        boolean userStatus=checkUserExist(uUID);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode resultNode = objectMapper.createObjectNode();

        if(userStatus){
            resultNode.put("uUID", uUID);
            
            Query q=new Query(Criteria.where("uUID").is(uUID));
            Customer c= mongotemplate.findOne(q, Customer.class);
            List<String> IdsToBeReturned= c.getBookmarks();
            //System.out.print(c);
            ArrayNode bookmarkArr=objectMapper.createArrayNode();

            for(String s: IdsToBeReturned){
                //System.out.println(s);
                bookmarkArr.add(s);
            }
            resultNode.set("Bookmarks", bookmarkArr);
        
        
        }
        return ResponseEntity.ok(resultNode);
        
    }
    

}
