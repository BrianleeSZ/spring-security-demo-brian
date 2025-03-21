package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/home";
    }
    
    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    // 创建admin和user的视图页面
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/user/dashboard")
    @PreAuthorize("hasRole('USER')")
    public String userDashboard() {
        return "user/dashboard";
    }

    // REST API端点使用@ResponseBody
    @GetMapping("/user")
    @ResponseBody
    @PreAuthorize("hasRole('USER')")
    public String user() {
        return "Welcome User!";
    }

    @GetMapping("/admin")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "Welcome Admin!";
    }
}