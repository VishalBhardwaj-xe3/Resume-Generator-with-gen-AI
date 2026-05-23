package com.intervi.resume.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/hello")
    public String hello() {
        String dbName = mongoTemplate.getDb().getName();
        return "spring boot is runnung" + dbName;
    }
    
}
