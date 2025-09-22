package com.example.StudentManagement.controller;

import com.example.StudentManagement.entity.Teacher;
import com.example.StudentManagement.service.CourseService;
import com.example.StudentManagement.service.TeacherService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final CourseService courseService;

    public TeacherController(TeacherService teacherService, CourseService courseService) {
        this.teacherService = teacherService;
        this.courseService = courseService;
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addTeacher(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam Integer courseId,
                             @RequestParam(required = false) String password, // Make sure this parameter exists
                             @RequestParam(required = false) String picName,
                             @RequestPart(name = "profilePicture", required = false) MultipartFile profilePicture) throws IOException {

        // DEBUG: Log all received parameters
        System.out.println("=== TEACHER CONTROLLER DEBUG ===");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("CourseId: " + courseId);
        System.out.println("Password received: " + (password != null ? "YES (length=" + password.length() + ")" : "NULL"));
        System.out.println("PicName: " + picName);
        System.out.println("ProfilePicture: " + (profilePicture != null ? profilePicture.getOriginalFilename() : "null"));

        validateCourseId(courseId);
        Teacher t = new Teacher(null, name.trim(), email.trim(), courseId);

        // DEBUG: Log Teacher object before service call
        System.out.println("Teacher object created - Password will be: " + (password != null ? "SET" : "NULL"));

        teacherService.addTeacher(t, picName, profilePicture, password);
        return "Teacher added successfully!";
    }


    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateTeacher(@RequestParam Integer id,
                                @RequestParam String name,
                                @RequestParam String email,
                                @RequestParam Integer courseId,
                                @RequestParam(required = false) String password,
                                @RequestParam(required = false) String picName,
                                @RequestPart(name = "profilePicture", required = false) MultipartFile profilePicture,
                                @RequestParam(defaultValue = "false") boolean removePicture) throws IOException {
        validateCourseId(courseId);
        Teacher t = new Teacher(id, name.trim(), email.trim(), courseId);
        teacherService.updateTeacher(t, picName, profilePicture, removePicture, password);
        return "Teacher updated successfully!";
    }

    @GetMapping("/all")
    public List<Object[]> getAllTeachers() {
        return teacherService.getTeachers();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable int id) {
        teacherService.deleteTeacher(id);
        return "Teacher deleted successfully!";
    }

    // NEW: Authentication endpoints (following your pattern)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email,
                                                     @RequestParam String password,
                                                     HttpSession session) {
        Teacher teacher = teacherService.authenticateTeacher(email, password);

        if (teacher == null) {
            return ResponseEntity.ok(Map.of("success", false, "message", "Invalid email or password"));
        }

        // Store in session
        session.setAttribute("teacherId", teacher.getId());
        session.setAttribute("teacherName", teacher.getName());
        session.setAttribute("courseId", teacher.getCourseId());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Login successful",
                "teacher", teacher
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/dashboard")
    @Transactional
    public ResponseEntity<Map<String, Object>> getDashboard(HttpSession session) {
        Integer teacherId = (Integer) session.getAttribute("teacherId");

        if (teacherId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }

        Teacher teacher = teacherService.getTeacherById(teacherId);
        if (teacher != null && teacher.getCourseId() != null) {
            String courseName = courseService.getCourseNameById(teacher.getCourseId());
            teacher.setCourseName(courseName);  // Works with @Transient annotation
        }
        List<Object[]> students = teacherService.getStudentsByCourse(teacher.getCourseId());

        return ResponseEntity.ok(Map.of(
                "teacher", teacher,
                "students", students
        ));
    }

    @GetMapping("/generate-password")
    public ResponseEntity<Map<String, String>> generatePassword() {
        String password = teacherService.generateRandomPassword();
        return ResponseEntity.ok(Map.of("password", password));
    }

    private void validateCourseId(Integer courseId) {
        if (courseId == null || courseService.getCourses().stream().noneMatch(c -> ((Integer) c[0]).equals(courseId))) {
            throw new IllegalArgumentException("Invalid course ID");
        }
    }
}
