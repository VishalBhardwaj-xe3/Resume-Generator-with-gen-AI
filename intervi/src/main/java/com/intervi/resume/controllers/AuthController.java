package com.intervi.resume.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.intervi.resume.models.User;
import com.intervi.resume.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired 
    private UserRepository UserRepository;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> create(@RequestBody User user) {
        if (UserRepository.findByEmail(user.getEmail()).isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Email already taken");
            return ResponseEntity.status(409).body(error);
        }

        UserRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "user registered successfully");
        response.put("name", user.getUsername());
        response.put("email", user.getEmail());
        return ResponseEntity.ok(response);
    } 
}
