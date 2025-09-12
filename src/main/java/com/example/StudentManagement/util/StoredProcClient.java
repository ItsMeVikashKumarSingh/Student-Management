package com.example.StudentManagement.util;

import com.example.StudentManagement.util.SpEnums.ActionType;
import com.example.StudentManagement.util.SpEnums.EntityType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoredProcClient {

    @PersistenceContext
    private EntityManager em;

    // Core builder
    private StoredProcedureQuery build(EntityType entity, ActionType action, String jsonPayload) {
        return em.createStoredProcedureQuery("sp_manage")
                .registerStoredProcedureParameter("p_entity", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_action", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_payload", String.class, ParameterMode.IN)
                .setParameter("p_entity", entity.code())
                .setParameter("p_action", action.code())
                .setParameter("p_payload", jsonPayload);
    }

    // Casting helper (first column scalar)
    @SuppressWarnings("unchecked")
    private <T> T castScalar(Object raw, Class<T> target) {
        if (raw == null) return null;
        Object v = (raw instanceof Object[]) ? ((Object[]) raw) : raw;
        if (target == Integer.class) return (T) Integer.valueOf(((Number) v).intValue());
        if (target == Long.class)    return (T) Long.valueOf(((Number) v).longValue());
        if (target == String.class)  return (T) String.valueOf(v);
        throw new IllegalArgumentException("Unsupported scalar type: " + target);
    }

    // Public API
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
}
