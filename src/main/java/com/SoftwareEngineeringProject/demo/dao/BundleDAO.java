package com.SoftwareEngineeringProject.demo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.SoftwareEngineeringProject.demo.entity.Bundle;

public interface BundleDAO extends MongoRepository<Bundle, String> {
    
}
