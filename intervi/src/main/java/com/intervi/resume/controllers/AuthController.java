package com.intervi.resume.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.intervi.resume.security.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.intervi.resume.models.BlackListToken;
import com.intervi.resume.models.User;
import com.intervi.resume.repository.BlackListedTokenRepository;
import com.intervi.resume.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired 
    private UserRepository UserRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BlackListedTokenRepository blackListedTokenRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> create(@RequestBody User user) {
        if (UserRepository.findByEmail(user.getEmail()).isPresent()
                || UserRepository.findByUsername(user.getUsername()).isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "User already exist with email address or username");
            return ResponseEntity.status(409).body(error);
        }

        String hashed = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashed);

        UserRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("message", "user registered successfully");
        response.put("token", token);
        response.put("name", user.getUsername());
        response.put("email", user.getEmail());
        response.put("Id", user.getId());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User loginRequest) {
        User user = UserRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("message", "User not found");
                    return new RuntimeException("User not found");
                });

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid password");
            return ResponseEntity.status(401).body(error);
        }

        String token = jwtUtil.generateToken(user.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("message", "user login successfully");
        response.put("token", token);
        response.put("name", user.getUsername());
        response.put("email", user.getEmail());
        response.put("Id", user.getId());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestAttribute("email") String email, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);

        BlackListToken blackListToken = new BlackListToken();
        blackListToken.setToken(token);
        blackListedTokenRepository.save(blackListToken);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User logout successfully");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMe(@RequestAttribute("email") String email) {

        User user = UserRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));

        Map<String, Object> response = new HashMap<>();

    response.put("id", user.getId());
    response.put("name", user.getUsername());
    response.put("email", user.getEmail());
    response.put("createdAt", user.getCreatedAt());
    return ResponseEntity.ok(response); 
    }
    

}
