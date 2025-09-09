package com.example.StudentManagement.service;

import com.example.StudentManagement.dao.CourseDao;
import com.example.StudentManagement.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseDao courseDao;

    @Transactional
    public void addCourse(Course course) {
        courseDao.addCourse(course);
    }

    @Transactional
    public List<Object[]> getCourses() {
        return courseDao.getCourses();
    }

    @Transactional
    public void updateCourse(Course course) {
        courseDao.updateCourse(course);
    }

    @Transactional
    public void deleteCourse(int id) {
        courseDao.deleteCourse(id);
    }
}
