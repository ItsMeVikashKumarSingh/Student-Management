package com.example.StudentManagement.controller;

import com.example.StudentManagement.entity.Student;
import com.example.StudentManagement.service.CourseService;
import com.example.StudentManagement.service.StudentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;

    public StudentController(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addStudent(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam Integer age,
                             @RequestParam Integer courseId,
                             @RequestParam(required = false) String picName,
                             @RequestPart(name = "profilePicture", required = false) MultipartFile profilePicture) throws IOException {
        validateCourseId(courseId);
        Student s = new Student(null, name.trim(), email.trim(), age, courseId);
        studentService.addStudent(s, picName, profilePicture);
        return "Student added successfully!";
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateStudent(@RequestParam Integer roll,
                                @RequestParam String name,
                                @RequestParam String email,
                                @RequestParam Integer age,
                                @RequestParam Integer courseId,
                                @RequestParam(required = false) String picName,
                                @RequestPart(name = "profilePicture", required = false) MultipartFile profilePicture) throws IOException {
        validateCourseId(courseId);
        Student s = new Student(roll, name.trim(), email.trim(), age, courseId);
        studentService.updateStudent(s, picName, profilePicture);
        return "Student updated successfully!";
    }

    @GetMapping("/all")
    public List<Object[]> getAllStudents() {
        return studentService.getStudents();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable int id) {
        studentService.deleteStudent(id);
        return "Student deleted successfully!";
    }

    private void validateCourseId(Integer courseId) {
        if (courseId == null || courseService.getCourses().stream().noneMatch(c -> ((Integer) c[0]).equals(courseId))) {
            throw new IllegalArgumentException("Invalid course ID");
        }
    }
}
