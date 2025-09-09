package com.example.StudentManagement.dao;

import com.example.StudentManagement.entity.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Course addCourse(Course course) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_add_course");
        query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_description", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_id", Integer.class, ParameterMode.OUT);

        query.setParameter("p_name", course.getName());
        query.setParameter("p_description", course.getDescription());

        query.execute();

        Integer newId = (Integer) query.getOutputParameterValue("p_id");
        course.setId(newId);
        return course;
    }

    public List<Object[]> getCourses() {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_get_courses");
        return query.getResultList();
    }

    public void updateCourse(Course course) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_update_course");
        query.registerStoredProcedureParameter("p_id", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_description", String.class, ParameterMode.IN);

        query.setParameter("p_id", course.getId());
        query.setParameter("p_name", course.getName());
        query.setParameter("p_description", course.getDescription());

        query.execute();
    }

    public void deleteCourse(int id) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_delete_course");
        query.registerStoredProcedureParameter("p_id", Integer.class, ParameterMode.IN);

        query.setParameter("p_id", id);

        query.execute();
    }
}
