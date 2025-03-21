package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String type) {
        
        User user;
        if (type != null && type.equals("admin")) {
            user = userService.registerUser(username, password, type);
        } else {
            user = userService.registerUser(username, password);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully!");
        response.put("username", user.getUsername());
        response.put("role", user.getRole().name());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}