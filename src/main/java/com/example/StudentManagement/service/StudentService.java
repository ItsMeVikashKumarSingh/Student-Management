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
        s.setProfilePictureName(null);

        Integer generatedRoll = dao.add(s, null);
        s.setRoll(generatedRoll);

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String finalName = storage.saveStudentImage(generatedRoll, picName, profilePicture);
            s.setProfilePictureName(finalName);
            dao.update(s, null);
        }

        return generatedRoll;
    }

    @Transactional
    public void updateStudent(Student input, String picName, MultipartFile profilePicture, boolean removePicture) throws IOException {
        Student existing = dao.getById(input.getRoll());

        existing.setName(input.getName());
        existing.setEmail(input.getEmail());
        existing.setAge(input.getAge());
        existing.setCourseId(input.getCourseId());

        String old = existing.getProfilePictureName();
        if (removePicture) {
            if (old != null) storage.deleteStudentImage(old);
            existing.setProfilePictureName(null);
        } else if (profilePicture != null && !profilePicture.isEmpty()) {
            if (old != null) storage.deleteStudentImage(old);
            String finalName = storage.saveStudentImage(existing.getRoll(), picName, profilePicture);
            existing.setProfilePictureName(finalName);
        }

        dao.update(existing, null);
    }

    @Transactional
    public List<Object[]> getStudents() {
        return dao.list();
    }

    @Transactional
    public void deleteStudent(int roll) {
        String old = dao.getPictureName(roll);
        if (old != null) storage.deleteStudentImage(old);
        dao.delete(roll, null);
    }

    @Transactional
    public List<Object[]> getStudentsByCourse(Integer courseId) {
        return dao.getByCourseId(courseId);
    }
}
