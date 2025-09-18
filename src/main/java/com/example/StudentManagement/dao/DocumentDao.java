package com.example.StudentManagement.dao;

import com.example.StudentManagement.entity.Document;
import com.example.StudentManagement.util.StoredProcClient;
import com.example.StudentManagement.util.SpEnums.EntityType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DocumentDao {

    private final StoredProcClient sp;

    public DocumentDao(StoredProcClient sp) { this.sp = sp; }

    // Upsert record for a student (ADD branch uses ON DUPLICATE KEY UPDATE)
    public Integer upsert(Document d) { return sp.add(EntityType.DOCUMENT, d); }

    public void update(Document d) { sp.update(EntityType.DOCUMENT, d); }

    public void deleteByStudentRoll(int roll) { sp.deleteByRoll(EntityType.DOCUMENT, roll); }

    public List<Object[]> list() { return sp.list(EntityType.DOCUMENT); }
}
