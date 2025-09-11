package com.example.StudentManagement.controller;

import com.example.StudentManagement.entity.Student;
import com.example.StudentManagement.service.CourseService;
import com.example.StudentManagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;  // Autowire CourseService for validation

    @PostMapping("/add")
    public String addStudent(@RequestBody Student student) {
        validateCourseId(student.getCourseId());
        studentService.addStudent(student);
        return "Student added successfully!";
    }

    @GetMapping("/all")
    public List<Object[]> getAllStudents() {
        return studentService.getStudents();
    }

    @PutMapping("/update")
    public String updateStudent(@RequestBody Student student) {
        validateCourseId(student.getCourseId());
        studentService.updateStudent(student);
        return "Student updated successfully!";
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
