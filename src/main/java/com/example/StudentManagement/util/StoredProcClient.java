package com.example.StudentManagement.util;

import com.example.StudentManagement.entity.Teacher;
import com.example.StudentManagement.util.SpEnums.ActionType;
import com.example.StudentManagement.util.SpEnums.EntityType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Component
public class StoredProcClient {

    @PersistenceContext
    private EntityManager em;

    // Core builder (UNCHANGED)
    private StoredProcedureQuery build(EntityType entity, ActionType action, String jsonPayload) {
        return em.createStoredProcedureQuery("sp_manage")
                .registerStoredProcedureParameter("p_entity", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_action", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_payload", String.class, ParameterMode.IN)
                .setParameter("p_entity", entity.code())
                .setParameter("p_action", action.code())
                .setParameter("p_payload", jsonPayload);
    }

    // Casting helper (UNCHANGED)
    @SuppressWarnings("unchecked")
    private <T> T castScalar(Object raw, Class<T> target) {
        if (raw == null) return null;
        Object v = (raw instanceof Object[]) ? ((Object[]) raw)[0] : raw;
        if (target == Integer.class) return (T) Integer.valueOf(((Number) v).intValue());
        if (target == Long.class)    return (T) Long.valueOf(((Number) v).longValue());
        if (target == String.class)  return (T) String.valueOf(v);
        throw new IllegalArgumentException("Unsupported scalar type: " + target);
    }

    // ALL YOUR EXISTING METHODS (UNCHANGED)
    public List<Object[]> list(EntityType entity) {
        return build(entity, ActionType.GET, null).getResultList();
    }

    public Integer add(EntityType entity, Object payload) {
        String json = SpPayloadUtil.save(payload);
        Object res = build(entity, ActionType.ADD, json).getSingleResult();
        return castScalar(res, Integer.class);
    }

    public void update(EntityType entity, Object payload) {
        String json = SpPayloadUtil.save(payload);
        build(entity, ActionType.UPDATE, json).getResultList();
    }

    public void deleteById(EntityType entity, int id) {
        build(entity, ActionType.DELETE, SpPayloadUtil.saveId(id)).getResultList();
    }

    public void deleteByRoll(EntityType entity, int roll) {
        build(entity, ActionType.DELETE, SpPayloadUtil.saveRoll(roll)).getResultList();
    }

    public String getPicNameById(EntityType entity, int id) {
        Object res = build(entity, ActionType.GET_PIC, SpPayloadUtil.saveId(id)).getSingleResult();
        return castScalar(res, String.class);
    }

    public String getPicNameByRoll(EntityType entity, int roll) {
        Object res = build(entity, ActionType.GET_PIC, SpPayloadUtil.saveRoll(roll)).getSingleResult();
        return castScalar(res, String.class);
    }

    public <T> T findById(Class<T> entityClass, int id) {
        return em.find(entityClass, id);
    }

    // NEW METHODS (ADDED)
    public Teacher findByEmail(Class<Teacher> clazz, String email) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", email);
        String json = SpPayloadUtil.save(payload);

        List<Object[]> result = build(EntityType.TEACHER, ActionType.LOGIN, json).getResultList();

        if (result.isEmpty()) return null;

        Object[] row = result.get(0);
        Teacher teacher = new Teacher();
        teacher.setId((Integer) row[0]);
        teacher.setName((String) row[1]);
        teacher.setEmail((String) row[2]);
        teacher.setPassword((String) row[3]); // For authentication
        teacher.setCourseName((String) row[4]);
        teacher.setCourseId((Integer) row[5]);
        teacher.setProfilePictureName((String) row[6]);

        return teacher;
    }

    public Teacher findTeacherById(int id) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", id);
        String json = SpPayloadUtil.save(payload);

        List<Object[]> result = build(EntityType.TEACHER, ActionType.GET_BY_ID, json).getResultList();

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

    public List<Object[]> getStudentsByCourse(Integer courseId) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("courseId", courseId);
        String json = SpPayloadUtil.save(payload);

        return build(EntityType.STUDENT, ActionType.GET_BY_COURSE, json).getResultList();
    }
}
