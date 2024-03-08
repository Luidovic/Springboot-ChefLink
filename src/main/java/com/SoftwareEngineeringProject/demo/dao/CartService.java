package com.SoftwareEngineeringProject.demo.dao;

import java.util.List;
import org.springframework.stereotype.Service;
import com.SoftwareEngineeringProject.demo.entity.Cart;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class CartService {
    private final CartDAO cartrepository;

    public CartService(CartDAO cartrepository){
        this.cartrepository=cartrepository;
    }

    @SuppressWarnings("null")
    public void saveCart(Cart cart){
        cartrepository.save(cart);
    }
    
    public List<Cart> getAllCarts(){
        return cartrepository.findAll();
    }

}
