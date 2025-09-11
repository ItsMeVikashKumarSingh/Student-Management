package com.example.StudentManagement.dao;

import com.example.StudentManagement.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Student addStudent(Student student) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_add_student");
        query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_age", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_course_id", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_roll", Integer.class, ParameterMode.OUT);

        query.setParameter("p_name", student.getName());
        query.setParameter("p_email", student.getEmail());
        query.setParameter("p_age", student.getAge());
        query.setParameter("p_course_id", student.getCourseId());

        query.execute();

        Integer newRoll = (Integer) query.getOutputParameterValue("p_roll");
        student.setRoll(newRoll);
        return student;
    }

    public List<Object[]> getStudents() {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_get_students");
        return query.getResultList();
    }

    public void updateStudent(Student student) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_update_student");
        query.registerStoredProcedureParameter("p_roll", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_age", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_course_id", Integer.class, ParameterMode.IN);

        query.setParameter("p_roll", student.getRoll());
        query.setParameter("p_name", student.getName());
        query.setParameter("p_email", student.getEmail());
        query.setParameter("p_age", student.getAge());
        query.setParameter("p_course_id", student.getCourseId());

        query.execute();
    }

    public void deleteStudent(int roll) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_delete_student");
        query.registerStoredProcedureParameter("p_roll", Integer.class, ParameterMode.IN);

        query.setParameter("p_roll", roll);

        query.execute();
    }
}
