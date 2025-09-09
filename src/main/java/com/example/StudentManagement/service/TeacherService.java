package com.example.StudentManagement.service;

import com.example.StudentManagement.dao.TeacherDao;
import com.example.StudentManagement.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherDao teacherDao;

    @Transactional
    public void addTeacher(Teacher teacher) {
        teacherDao.addTeacher(teacher);
    }

    @Transactional
    public List<Object[]> getTeachers() {
        return teacherDao.getTeachers();
    }

    @Transactional
    public void updateTeacher(Teacher teacher) {
        teacherDao.updateTeacher(teacher);
    }

    @Transactional
    public void deleteTeacher(int id) {
        teacherDao.deleteTeacher(id);
    }
}
