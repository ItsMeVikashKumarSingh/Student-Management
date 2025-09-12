package com.example.StudentManagement.service;

import com.example.StudentManagement.dao.StudentDao;
import com.example.StudentManagement.entity.Student;
import com.example.StudentManagement.storage.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class StudentService {

    private final StudentDao dao;
    private final FileStorageService storage;

    public StudentService(StudentDao dao, FileStorageService storage) {
        this.dao = dao;
        this.storage = storage;
    }

    @Transactional
    public Integer addStudent(Student s, String picName, MultipartFile profilePicture) throws IOException {
        // Insert first to get ID
        s.setProfilePictureName(null);
        Integer id = dao.add(s);
        s.setRoll(id);

        // If image provided, save file and update only the picture field via UPDATE
        if (profilePicture != null && !profilePicture.isEmpty()) {
            String finalName = storage.saveStudentImage(id, picName, profilePicture);
            s.setProfilePictureName(finalName);
            dao.update(s);
        }
        return id;
    }

    @Transactional
    public void updateStudent(Student s, String picName, MultipartFile profilePicture) throws IOException {
        if (profilePicture != null && !profilePicture.isEmpty()) {
            String old = dao.getPictureName(s.getRoll());
            if (old != null) storage.deleteStudentImage(old);
            String finalName = storage.saveStudentImage(s.getRoll(), picName, profilePicture);
            s.setProfilePictureName(finalName);
        }
        dao.update(s);
    }

    @Transactional
    public void deleteStudent(int roll) {
        String old = dao.getPictureName(roll);
        dao.delete(roll);
        if (old != null) storage.deleteStudentImage(old);
    }

    @Transactional
    public List<Object[]> getStudents() {
        return dao.list();
    }
}
