package com.SoftwareEngineeringProject.demo.dao;

import com.SoftwareEngineeringProject.demo.entity.Customer;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CustomerDAO extends MongoRepository<Customer, String> {

    public Customer findByFirstName(String FirstName);

    public List<Customer> findByLastName(String LastName);

    Customer findByUsername(String username);

            

}
