package com.example.StudentManagement.dao;

import com.example.StudentManagement.entity.Student;
import com.example.StudentManagement.util.StoredProcClient;
import com.example.StudentManagement.util.SpEnums.EntityType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentDao {

    private final StoredProcClient sp;

    public StudentDao(StoredProcClient sp) { this.sp = sp; }

    public Integer add(Student s) { return sp.add(EntityType.STUDENT, s); }

    public void update(Student s) { sp.update(EntityType.STUDENT, s); }

    public void delete(int roll) { sp.deleteByRoll(EntityType.STUDENT, roll); }

    public String getPictureName(int roll) { return sp.getPicNameByRoll(EntityType.STUDENT, roll); }

    public List<Object[]> list() { return sp.list(EntityType.STUDENT); }
}
