package com.SoftwareEngineeringProject.demo.dao;

import java.util.List;

import org.springframework.stereotype.Service;

import com.SoftwareEngineeringProject.demo.entity.Bundle;

@Service
public class BundleService {
    private final BundleDAO bundleDAO;

    public BundleService(BundleDAO bundleDAO) {
        this.bundleDAO = bundleDAO;
    }

    public List<Bundle> getAllBundles(){
        return bundleDAO.findAll();
    }
}
