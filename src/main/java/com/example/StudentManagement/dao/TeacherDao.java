package com.example.StudentManagement.dao;

import com.example.StudentManagement.entity.Teacher;
import com.example.StudentManagement.util.StoredProcClient;
import com.example.StudentManagement.util.SpEnums.EntityType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeacherDao {

    private final StoredProcClient sp;

    public TeacherDao(StoredProcClient sp) { this.sp = sp; }

    public Integer add(Teacher t) { return sp.add(EntityType.TEACHER, t); }

    public void update(Teacher t) { sp.update(EntityType.TEACHER, t); }

    public void delete(int id) { sp.deleteById(EntityType.TEACHER, id); }

    public String getPictureName(int id) { return sp.getPicNameById(EntityType.TEACHER, id); }

    public List<Object[]> list() { return sp.list(EntityType.TEACHER); }
}
