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
    public Integer addCourse(Course course) {
        Integer generatedId = dao.addCourse(course, null);
        course.setId(generatedId);
        return generatedId;
    }

    @Transactional
    public List<Object[]> getCourses() {
        return dao.getCourses();
    }

    @Transactional
    public void updateCourse(Course course) {
        dao.updateCourse(course, null);
    }

    @Transactional
    public void deleteCourse(int id) {
        dao.deleteCourse(id, null);
    }

    public String getCourseNameById(int courseId) {
        return dao.getCourseNameById(courseId);
    }
}
