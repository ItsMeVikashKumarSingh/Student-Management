package com.example.StudentManagement.controller;

import com.example.StudentManagement.dao.TeacherDao;
import com.example.StudentManagement.entity.Teacher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public UnifiedAuthController(AuthenticationManager authenticationManager, TeacherDao teacherDao) {
        this.authenticationManager = authenticationManager;
        this.teacherDao = teacherDao;
    }

    @PostMapping("/authenticate")
    @ResponseBody
    public Map<String, Object> authenticate(@RequestParam String username,
                                            @RequestParam String password,
                                            HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("Login attempt for: " + username);

            // Authenticate via manager (handles BCrypt for all users)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Set authentication and save to session
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            System.out.println("Authentication successful, session created");

            response.put("success", true);
            response.put("authType", "SESSION");

            // Customize response based on user type
            String userType = determineUserType(username, session, response);
            response.put("userType", userType);
            response.put("redirectUrl", "TEACHER".equals(userType) ? "/teacher-dashboard.html" : "/dashboard.html");
            response.put("message", getWelcomeMessage(userType, username));

            return response;
        } catch (BadCredentialsException e) {
            System.err.println("Bad credentials for: " + username);
            response.put("success", false);
            response.put("message", "Invalid username or password");
            return response;
        } catch (AuthenticationException e) {
            System.err.println("Auth error: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Authentication error: " + e.getMessage());
            return response;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Authentication failed: " + e.getMessage());
            return response;
        }
    }

    // Helper to determine user type and set session/response details
    private String determineUserType(String username, HttpSession session, Map<String, Object> response) {
        if ("admin".equals(username)) {
            return "ADMIN";
        } else if ("user".equals(username)) {
            session.setAttribute("username", username);  // Optional, if needed for non-teacher users
            return "USER";
        } else {
            // Load teacher details (post-auth)
            Teacher teacher = teacherDao.getByEmail(username);
            if (teacher == null) {
                throw new RuntimeException("Teacher not found after successful auth");  // Should not happen
            }
            session.setAttribute("teacherId", teacher.getId());
            session.setAttribute("teacherName", teacher.getName());
            session.setAttribute("teacherEmail", teacher.getEmail());
            response.put("user", Map.of(
                    "id", teacher.getId(),
                    "name", teacher.getName(),
                    "email", teacher.getEmail(),
                    "role", "TEACHER"
            ));
            return "TEACHER";
        }
    }

    // Helper for welcome message
    private String getWelcomeMessage(String userType, String username) {
        return switch (userType) {
            case "ADMIN" -> "Admin login successful! Welcome Administrator.";
            case "USER" -> "User login successful! Welcome User.";
            case "TEACHER" -> "Teacher login successful! Welcome " + username + ".";
            default -> "Login successful!";
        };
    }

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
