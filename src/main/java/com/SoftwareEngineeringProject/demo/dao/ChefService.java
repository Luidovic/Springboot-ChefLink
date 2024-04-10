package com.SoftwareEngineeringProject.demo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.SoftwareEngineeringProject.demo.entity.Chef;
import com.SoftwareEngineeringProject.demo.entity.Customer;

@Service
public class ChefService {

    private final ChefDAO chefRepository;
    private final MongoTemplate mongotemplate;

    public ChefService(ChefDAO chefRepository, MongoTemplate mongotemplate) {
        this.chefRepository = chefRepository;
        this.mongotemplate = mongotemplate;
    }

    public void saveChef(Chef chef) {
        chefRepository.save(chef);
    }

    public List<Chef> getAll() {
        return chefRepository.findAll();
    }

    public void addBookmark(String userId, String foodId) {

        // Add other fields to update as needed

        Chef chef = chefRepository.findByUsernameChef(userId);
        if (chef != null) {
            Query query = Query.query(Criteria.where("usernameChef").is(userId));
            Update update = new Update();
            List<String> bookmarks = chef.getBookmarks();
            if (bookmarks == null) {
                bookmarks = new ArrayList<>();
            }
            bookmarks.add(foodId);
            update.set("bookmarks", bookmarks);
            mongotemplate.updateFirst(query, update, Chef.class);

        } else {

        }
    }

    public Chef findChefById(String uUID) {
        return chefRepository.findByuUID(uUID);
    }

}