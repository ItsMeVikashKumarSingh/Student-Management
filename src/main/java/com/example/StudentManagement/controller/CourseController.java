package com.example.StudentManagement.controller;

import com.example.StudentManagement.entity.Course;
import com.example.StudentManagement.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/add")
    public String addCourse(@RequestBody Course course) {
        courseService.addCourse(course);
        return "Course added successfully!";
    }

    @GetMapping("/all")
    public List<Object[]> getAllCourses() {
        return courseService.getCourses();
    }

    @PutMapping("/update")
    public String updateCourse(@RequestBody Course course) {
        courseService.updateCourse(course);
        return "Course updated successfully!";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        return "Course deleted successfully!";
    }

    @GetMapping("/suggestions")
    public List<Object[]> getCourseSuggestions() {
        return courseService.getCourses();  // Returns [id, name, description]
    }

}
