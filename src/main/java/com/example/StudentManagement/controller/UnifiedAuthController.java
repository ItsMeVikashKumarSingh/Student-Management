package com.example.StudentManagement.controller;

import com.example.StudentManagement.entity.Teacher;
import com.example.StudentManagement.dao.TeacherDao;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class UnifiedAuthController {

    private final AuthenticationManager authenticationManager;
    private final TeacherDao teacherDao;
    private final BCryptPasswordEncoder passwordEncoder;

    public UnifiedAuthController(AuthenticationManager authenticationManager,
                                 TeacherDao teacherDao,
                                 BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.teacherDao = teacherDao;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/authenticate")
    @ResponseBody
    public Map<String, Object> authenticate(@RequestParam String username,
                                            @RequestParam String password,
                                            HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("Login attempt for: " + username);

            // Check if it's admin or user (session-based authentication)
            if ("admin".equals(username) || "user".equals(username)) {
                return authenticateAdminUser(username, password, request, response);
            } else {
                return authenticateTeacher(username, password, request, response);
            }
        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();

            response.put("success", false);
            response.put("message", "Authentication failed: " + e.getMessage());
            return response;
        }
    }

    // **SIMPLE FIX: Direct session management**
    private Map<String, Object> authenticateAdminUser(String username, String password,
                                                      HttpServletRequest request,
                                                      Map<String, Object> response) {
        try {
            System.out.println("Attempting admin authentication for: " + username);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // **KEY FIX: Set authentication and manually save to session**
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Manually save SecurityContext to session
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            System.out.println("Admin authentication successful, session created");

            response.put("success", true);
            response.put("userType", username.toUpperCase());
            response.put("authType", "SESSION");
            response.put("redirectUrl", "/dashboard.html");

            if ("admin".equals(username)) {
                response.put("message", "Admin login successful! Welcome Administrator.");
            } else {
                response.put("message", "User login successful! Welcome User.");
            }

            return response;
        } catch (BadCredentialsException e) {
            System.err.println("Bad credentials for: " + username);
            response.put("success", false);
            response.put("message", "Invalid username or password for " + username + " account");
            return response;
        } catch (Exception e) {
            System.err.println("Admin auth error: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Authentication error: " + e.getMessage());
            return response;
        }
    }

    // Session-based authentication for teachers
    private Map<String, Object> authenticateTeacher(String emailOrUsername, String password,
                                                    HttpServletRequest request,
                                                    Map<String, Object> response) {
        try {
            System.out.println("Attempting teacher authentication for: " + emailOrUsername);

            Teacher teacher = teacherDao.getByEmail(emailOrUsername);

            if (teacher == null) {
                response.put("success", false);
                response.put("userNotFound", true);
                response.put("message", "No teacher account found with email: " + emailOrUsername);
                response.put("suggestion", "Please check your email address or contact administrator.");
                return response;
            }

            if (passwordEncoder.matches(password, teacher.getPassword())) {
                // Create authentication for teacher
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        teacher.getEmail(),
                        null,
                        java.util.List.of(() -> "ROLE_TEACHER")
                );

                // Set authentication and save to session
                SecurityContextHolder.getContext().setAuthentication(auth);

                HttpSession session = request.getSession(true);
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        SecurityContextHolder.getContext());

                // Store teacher info for easy access
                session.setAttribute("teacherId", teacher.getId());
                session.setAttribute("teacherName", teacher.getName());
                session.setAttribute("teacherEmail", teacher.getEmail());

                response.put("success", true);
                response.put("userType", "TEACHER");
                response.put("authType", "SESSION");
                response.put("redirectUrl", "/teacher-dashboard.html");
                response.put("user", Map.of(
                        "id", teacher.getId(),
                        "name", teacher.getName(),
                        "email", teacher.getEmail(),
                        "role", "TEACHER"
                ));
                response.put("message", "Teacher login successful! Welcome " + teacher.getName() + ".");

                return response;
            } else {
                response.put("success", false);
                response.put("wrongPassword", true);
                response.put("message", "Incorrect password for " + teacher.getName());
                response.put("suggestion", "Please check your password or contact administrator.");
                return response;
            }
        } catch (Exception e) {
            System.err.println("Teacher auth error: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Teacher authentication error: " + e.getMessage());
            return response;
        }
    }
    // Add this method to your UnifiedAuthController.java

    @GetMapping("/api/session-check")
    @ResponseBody
    public ResponseEntity<?> checkSession(HttpSession session) {
        Integer teacherId = (Integer) session.getAttribute("teacherId");
        String username = (String) session.getAttribute("username");

        if (teacherId != null || username != null) {
            return ResponseEntity.ok(Map.of("valid", true));
        } else {
            return ResponseEntity.status(401).body(Map.of("valid", false, "message", "Session expired"));
        }
    }

}
