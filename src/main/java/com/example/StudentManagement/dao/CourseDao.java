package com.example.StudentManagement.dao;

import com.example.StudentManagement.entity.Course;
import com.example.StudentManagement.util.StoredProcClient;
import com.example.StudentManagement.util.SpEnums.EntityType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDao {

    private final StoredProcClient sp;

    public CourseDao(StoredProcClient sp) { this.sp = sp; }

    public Integer addCourse(Course course) { return sp.add(EntityType.COURSE, course); }

    public void updateCourse(Course course) { sp.update(EntityType.COURSE, course); }

    public void deleteCourse(int id) { sp.deleteById(EntityType.COURSE, id); }

    public List<Object[]> getCourses() { return sp.list(EntityType.COURSE); }
}
