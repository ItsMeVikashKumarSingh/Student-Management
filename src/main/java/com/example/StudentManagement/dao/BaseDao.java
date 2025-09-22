package com.example.StudentManagement.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BaseDao {

    @PersistenceContext
    protected EntityManager em;

    protected void setSessionVar(String varName, Object value) {
        if (value != null) {
            em.createNativeQuery("SET @" + varName + " = ?")
                    .setParameter(1, value)
                    .executeUpdate();
        }
    }

    protected Object callStoredProc(String actionType, String actionValue) {
        return em.createStoredProcedureQuery("sp_manage")
                .registerStoredProcedureParameter("actionType", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("actionValue", String.class, ParameterMode.IN)
                .setParameter("actionType", actionType)
                .setParameter("actionValue", actionValue)
                .getSingleResult();
    }

    protected List<Object[]> callStoredProcList(String actionType, String actionValue) {
        return em.createStoredProcedureQuery("sp_manage")
                .registerStoredProcedureParameter("actionType", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("actionValue", String.class, ParameterMode.IN)
                .setParameter("actionType", actionType)
                .setParameter("actionValue", actionValue)
                .getResultList();
    }

    protected void callStoredProcNoResult(String actionType, String actionValue) {
        em.createStoredProcedureQuery("sp_manage")
                .registerStoredProcedureParameter("actionType", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("actionValue", String.class, ParameterMode.IN)
                .setParameter("actionType", actionType)
                .setParameter("actionValue", actionValue)
                .execute();
    }
}
