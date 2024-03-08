package com.SoftwareEngineeringProject.demo.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SoftwareEngineeringProject.demo.dao.BundleService;
import com.SoftwareEngineeringProject.demo.entity.Bundle;

@RestController
@RequestMapping("/api/bundle")
public class BundleController {
    private BundleService bundleService;

    public BundleController(BundleService bundleService) {
        this.bundleService = bundleService;
    }

    @GetMapping("/getall")
    public List<Bundle> getAllBundles() {
        List<Bundle> list = bundleService.getAllBundles();
        System.out.println(list);
        return list;
    }
}
