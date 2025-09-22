package com.example.StudentManagement.dao;

import com.example.StudentManagement.entity.Course;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDao extends BaseDao {

    public Integer addCourse(Course course, String dynamicSql) {
        setSessionVar("p_courseName", course.getName());
        setSessionVar("p_courseDescription", course.getDescription());

        Object result = callStoredProc("addcourse", dynamicSql);
        return result != null ? ((Number) result).intValue() : null;
    }

    public void updateCourse(Course course, String dynamicSql) {
        setSessionVar("p_courseId", course.getId());
        setSessionVar("p_courseName", course.getName());
        setSessionVar("p_courseDescription", course.getDescription());

        callStoredProcNoResult("updatecourse", dynamicSql);
    }

    public void deleteCourse(int id, String dynamicSql) {
        setSessionVar("p_courseId", id);
        callStoredProcNoResult("deletecourse", dynamicSql);
    }

    public List<Object[]> getCourses() {
        return callStoredProcList("getcourses", null);
    }

    public String getCourseNameById(int courseId) {
        setSessionVar("p_courseId", courseId);
        Object result = callStoredProc("getcoursename", null);
        return result != null ? result.toString() : null;
    }
}
