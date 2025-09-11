package com.example.StudentManagement.dao;

import com.example.StudentManagement.entity.Teacher;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeacherDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Teacher addTeacher(Teacher teacher) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_add_teacher");
        query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_course_id", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_id", Integer.class, ParameterMode.OUT);

        query.setParameter("p_name", teacher.getName());
        query.setParameter("p_email", teacher.getEmail());
        query.setParameter("p_course_id", teacher.getCourseId());

        query.execute();

        Integer newId = (Integer) query.getOutputParameterValue("p_id");
        teacher.setId(newId);
        return teacher;
    }

    public List<Object[]> getTeachers() {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_get_teachers");
        return query.getResultList();
    }

    public void updateTeacher(Teacher teacher) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_update_teacher");
        query.registerStoredProcedureParameter("p_id", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_course_id", Integer.class, ParameterMode.IN);

        query.setParameter("p_id", teacher.getId());
        query.setParameter("p_name", teacher.getName());
        query.setParameter("p_email", teacher.getEmail());
        query.setParameter("p_course_id", teacher.getCourseId());

        query.execute();
    }

    public void deleteTeacher(int id) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_delete_teacher");
        query.registerStoredProcedureParameter("p_id", Integer.class, ParameterMode.IN);

        query.setParameter("p_id", id);

        query.execute();
    }
}
