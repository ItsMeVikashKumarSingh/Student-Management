package com.example.StudentManagement.controller;

import com.example.StudentManagement.entity.Teacher;
import com.example.StudentManagement.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/add")
    public String addTeacher(@RequestBody Teacher teacher) {
        teacherService.addTeacher(teacher);
        return "Teacher added successfully!";
    }

    @GetMapping("/all")
    public List<Object[]> getAllTeachers() {
        return teacherService.getTeachers();
    }

    @PutMapping("/update")
    public String updateTeacher(@RequestBody Teacher teacher) {
        teacherService.updateTeacher(teacher);
        return "Teacher updated successfully!";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable int id) {
        teacherService.deleteTeacher(id);
        return "Teacher deleted successfully!";
    }
}
