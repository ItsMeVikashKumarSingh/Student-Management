package com.example.StudentManagement.service;

import com.example.StudentManagement.dao.StudentDao;
import com.example.StudentManagement.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentDao studentDao;
@Transactional
    public void addStudent(Student student) {
        studentDao.addStudent(student);
    }
@Transactional
    public List<Object[]> getStudents() {
        return studentDao.getStudents();
    }
    @Transactional

    public void updateStudent(Student student) {
        studentDao.updateStudent(student);
    }
    @Transactional

    public void deleteStudent(int roll) {
        studentDao.deleteStudent(roll);
    }
}
