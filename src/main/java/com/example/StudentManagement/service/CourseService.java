package com.example.StudentManagement.service;

import com.example.StudentManagement.dao.CourseDao;
import com.example.StudentManagement.entity.Course;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    private final CourseDao dao;

    public CourseService(CourseDao dao) {
        this.dao = dao;
    }

    @Transactional
    public void addCourse(Course course) {
        Integer id = dao.addCourse(course);
        course.setId(id);
    }

    @Transactional
    public List<Object[]> getCourses() {
        return dao.getCourses();
    }

    @Transactional
    public void updateCourse(Course course) {
        dao.updateCourse(course);
    }

    @Transactional
    public void deleteCourse(int id) {
        dao.deleteCourse(id);
    }
}
