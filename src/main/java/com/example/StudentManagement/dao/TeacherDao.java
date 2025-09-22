package com.example.StudentManagement.dao;

import com.example.StudentManagement.entity.Teacher;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeacherDao extends BaseDao {

    public Integer add(Teacher t, String dynamicSql) {
        setSessionVar("p_teacherName", t.getName());
        setSessionVar("p_teacherEmail", t.getEmail());
        setSessionVar("p_courseId", t.getCourseId());
        setSessionVar("p_profilePictureName", t.getProfilePictureName());
        setSessionVar("p_teacherPassword", t.getPassword());

        Object result = callStoredProc("addteacher", dynamicSql);
        return result != null ? ((Number) result).intValue() : null;
    }

    public void update(Teacher t, String dynamicSql) {
        setSessionVar("p_teacherId", t.getId());
        setSessionVar("p_teacherName", t.getName());
        setSessionVar("p_teacherEmail", t.getEmail());
        setSessionVar("p_courseId", t.getCourseId());
        setSessionVar("p_profilePictureName", t.getProfilePictureName());
        setSessionVar("p_teacherPassword", t.getPassword());

        callStoredProcNoResult("updateteacher", dynamicSql);
    }

    public void delete(int id, String dynamicSql) {
        setSessionVar("p_teacherId", id);
        callStoredProcNoResult("deleteteacher", dynamicSql);
    }

    public String getPictureName(int id) {
        setSessionVar("p_teacherId", id);
        Object result = callStoredProc("getteacherpicname", null);
        return result != null ? result.toString() : null;
    }

    public List<Object[]> list() {
        return callStoredProcList("getteachers", null);
    }

    public Teacher getById(int id) {
        setSessionVar("p_teacherId", id);

        List<Object[]> result = callStoredProcList("getteacherbyid", null);

        if (result.isEmpty()) return null;

        Object[] row = result.get(0);
        Teacher teacher = new Teacher();
        teacher.setId((Integer) row[0]);
        teacher.setName((String) row[1]);
        teacher.setEmail((String) row[2]);
        teacher.setCourseName((String) row[3]);
        teacher.setCourseId((Integer) row[4]);
        teacher.setProfilePictureName((String) row[5]);

        return teacher;
    }

    public Teacher getByEmail(String email) {
        setSessionVar("p_teacherEmail", email);

        List<Object[]> result = callStoredProcList("teacherlogin", null);

        if (result.isEmpty()) return null;

        Object[] row = result.get(0);
        Teacher teacher = new Teacher();
        teacher.setId((Integer) row[0]);
        teacher.setName((String) row[1]);
        teacher.setEmail((String) row[2]);
        teacher.setPassword((String) row[3]);
        teacher.setCourseName((String) row[4]);
        teacher.setCourseId((Integer) row[5]);
        teacher.setProfilePictureName((String) row[6]);

        return teacher;
    }

    public List<Object[]> getStudentsByCourseId(Integer courseId) {
        setSessionVar("p_courseId", courseId);
        return callStoredProcList("getstudentsbycourse", null);
    }
}
