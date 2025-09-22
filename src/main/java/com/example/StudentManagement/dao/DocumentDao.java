package com.example.StudentManagement.dao;

import com.example.StudentManagement.entity.Document;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DocumentDao extends BaseDao {

    public Integer upsert(Document d, String dynamicSql) {
        setSessionVar("p_studentRoll", d.getStudentRoll());
        setSessionVar("p_aadharName", d.getAadharName());
        setSessionVar("p_panName", d.getPanName());
        setSessionVar("p_marksheetName", d.getMarksheetName());
        setSessionVar("p_aadharUploaded", d.getAadharUploaded() ? 1 : 0);
        setSessionVar("p_panUploaded", d.getPanUploaded() ? 1 : 0);
        setSessionVar("p_marksheetUploaded", d.getMarksheetUploaded() ? 1 : 0);

        Object result = callStoredProc("adddocument", dynamicSql);
        return result != null ? ((Number) result).intValue() : null;
    }

    public void update(Document d, String dynamicSql) {
        setSessionVar("p_studentRoll", d.getStudentRoll());
        setSessionVar("p_aadharName", d.getAadharName());
        setSessionVar("p_panName", d.getPanName());
        setSessionVar("p_marksheetName", d.getMarksheetName());
        setSessionVar("p_aadharUploaded", d.getAadharUploaded() ? 1 : 0);
        setSessionVar("p_panUploaded", d.getPanUploaded() ? 1 : 0);
        setSessionVar("p_marksheetUploaded", d.getMarksheetUploaded() ? 1 : 0);

        callStoredProcNoResult("updatedocument", dynamicSql);
    }

    public void deleteByStudentRoll(int roll, String dynamicSql) {
        setSessionVar("p_studentRoll", roll);
        callStoredProcNoResult("deletedocument", dynamicSql);
    }

    public List<Object[]> list() {
        return callStoredProcList("getdocuments", null);
    }
}
