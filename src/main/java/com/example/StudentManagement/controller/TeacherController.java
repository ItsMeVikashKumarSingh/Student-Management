package com.example.StudentManagement.controller;

import com.example.StudentManagement.entity.Teacher;
import com.example.StudentManagement.service.CourseService;
import com.example.StudentManagement.service.TeacherService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
                             @RequestParam(required = false) String picName,
                             @RequestPart(name = "profilePicture", required = false) MultipartFile profilePicture) throws IOException {
        validateCourseId(courseId);
        Teacher t = new Teacher(null, name.trim(), email.trim(), courseId);
        teacherService.addTeacher(t, picName, profilePicture);
        return "Teacher added successfully!";
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateTeacher(@RequestParam Integer id,
                                @RequestParam String name,
                                @RequestParam String email,
                                @RequestParam Integer courseId,
                                @RequestParam(required = false) String picName,
                                @RequestPart(name = "profilePicture", required = false) MultipartFile profilePicture,
                                @RequestParam(defaultValue = "false") boolean removePicture) throws IOException {
        validateCourseId(courseId);
        Teacher t = new Teacher(id, name.trim(), email.trim(), courseId);
        teacherService.updateTeacher(t, picName, profilePicture, removePicture);
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

    private void validateCourseId(Integer courseId) {
        if (courseId == null || courseService.getCourses().stream().noneMatch(c -> ((Integer) c[0]).equals(courseId))) {
            throw new IllegalArgumentException("Invalid course ID");
        }
    }
}
