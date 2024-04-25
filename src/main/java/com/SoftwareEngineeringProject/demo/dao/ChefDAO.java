package com.SoftwareEngineeringProject.demo.dao;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.SoftwareEngineeringProject.demo.entity.Chef;



public interface ChefDAO extends MongoRepository<Chef, String> {
    
    public Chef findByFirstName(String FirstName);

    public List<Chef> findByLastName(String LastName);
    
    Chef findByUsername(String usernameChef);
    
    Chef findByuUID(String uUID);

}
