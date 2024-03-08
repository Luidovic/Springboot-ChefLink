package com.SoftwareEngineeringProject.demo.rest;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SoftwareEngineeringProject.demo.dao.CartDAO;
import com.SoftwareEngineeringProject.demo.dao.CartService;
import com.SoftwareEngineeringProject.demo.entity.Cart;
import com.SoftwareEngineeringProject.demo.entity.Customer;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/api")
public class CartRestController {
    private CartDAO cartdao;
    private CartService cartService;
    private MongoTemplate mongotemplate;

    public CartRestController(CartDAO cartdao, CartService cartService, MongoTemplate mongotemplate) {
        this.cartdao = cartdao;
        this.cartService = cartService;
        this.mongotemplate = mongotemplate;
    }

    @PostMapping("/Cart/Add")
    public ResponseEntity<String> addCart(@RequestBody Cart cart) {
        System.out.println(cart.getiD());
        cartService.saveCart(cart);
        return new ResponseEntity<>("Cart Added Successfully", HttpStatus.CREATED);
    }

    @GetMapping("/Cart")
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }

}
