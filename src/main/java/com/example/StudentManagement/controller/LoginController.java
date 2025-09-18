package com.example.StudentManagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/teacher-login")
    public String teacherLogin() {
        return "teacher-login.html";
    }

    @GetMapping("/teacher-dashboard")
    public String teacherDashboard() {
        return "teacher-dashboard.html";
    }
}
