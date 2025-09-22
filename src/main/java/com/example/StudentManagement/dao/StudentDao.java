package com.example.StudentManagement.dao;

import com.example.StudentManagement.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentDao extends BaseDao {

    public Integer add(Student s, String dynamicSql) {
        setSessionVar("p_studentName", s.getName());
        setSessionVar("p_studentEmail", s.getEmail());
        setSessionVar("p_studentAge", s.getAge());
        setSessionVar("p_courseId", s.getCourseId());
        setSessionVar("p_profilePictureName", s.getProfilePictureName());

        Object result = callStoredProc("addstudent", dynamicSql);
        return result != null ? ((Number) result).intValue() : null;
    }

    public void update(Student s, String dynamicSql) {
        setSessionVar("p_studentRoll", s.getRoll());
        setSessionVar("p_studentName", s.getName());
        setSessionVar("p_studentEmail", s.getEmail());
        setSessionVar("p_studentAge", s.getAge());
        setSessionVar("p_courseId", s.getCourseId());
        setSessionVar("p_profilePictureName", s.getProfilePictureName());

        callStoredProcNoResult("updatestudent", dynamicSql);
    }

    public void delete(int roll, String dynamicSql) {
        setSessionVar("p_studentRoll", roll);
        callStoredProcNoResult("deletestudent", dynamicSql);
    }

    public String getPictureName(int roll) {
        setSessionVar("p_studentRoll", roll);
        Object result = callStoredProc("getstudentpicname", null);
        return result != null ? result.toString() : null;
    }

    public Student getById(int roll) {
        return em.find(Student.class, roll);
    }

    public List<Object[]> list() {
        return callStoredProcList("getstudents", null);
    }

    public List<Object[]> getByCourseId(Integer courseId) {
        setSessionVar("p_courseId", courseId);
        return callStoredProcList("getstudentsbycourse", null);
    }
}
