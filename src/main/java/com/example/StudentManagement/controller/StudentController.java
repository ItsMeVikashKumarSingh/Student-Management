package com.example.StudentManagement.controller;

import com.example.StudentManagement.entity.Student;
import com.example.StudentManagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/add")
    public String addStudent(@RequestBody Student student) {
        studentService.addStudent(student);
        return "Student added successfully!";
    }

    @GetMapping("/all")
    public List<Object[]> getAllStudents() {
        return studentService.getStudents();
    }

    @PutMapping("/update")
    public String updateStudent(@RequestBody Student student) {
        studentService.updateStudent(student);
        return "Student updated successfully!";
    }

    @DeleteMapping("/delete/{roll}")
    public String deleteStudent(@PathVariable int roll) {
        studentService.deleteStudent(roll);
        return "Student deleted successfully!";
    }
}
