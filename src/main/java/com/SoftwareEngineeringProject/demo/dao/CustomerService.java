package com.SoftwareEngineeringProject.demo.dao;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.SoftwareEngineeringProject.demo.entity.Customer;

import java.util.ArrayList;
//import java.io.IOException;
//import java.io.InputStream;
import java.util.List;
// import java.util.Optional;
// import java.util.function.Function;
import java.util.Optional;

// import org.springframework.core.io.ClassPathResource;
// import org.springframework.core.io.Resource;
// import org.springframework.data.domain.Example;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
// import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
// import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerDAO customerRepository;
    private final MongoTemplate mongoTemplate;

    public CustomerService(CustomerDAO customerRepository, MongoTemplate mongoTemplate) {
        this.customerRepository = customerRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @SuppressWarnings("null")
    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public void addBookmark(String userId, String foodId) {

        // Add other fields to update as needed

        Customer customer = customerRepository.findByUsername(userId);
        if (customer != null) {
            Query query = Query.query(Criteria.where("username").is(userId));
            Update update = new Update();
            List<String> bookmarks = customer.getBookmarks();
            if (bookmarks == null) {
                bookmarks = new ArrayList<>();
            }
            bookmarks.add(foodId);
            update.set("bookmarks", bookmarks);
            mongoTemplate.updateFirst(query, update, Customer.class);

        } else {
            // Handle user not found scenario
        }
    }

    public Customer findCustomerById(String customerId) {
        return customerRepository.findByuUID(customerId);
    }

}