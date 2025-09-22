package com.example.StudentManagement.service;

import com.example.StudentManagement.dao.TeacherDao;
import com.example.StudentManagement.entity.Teacher;
import com.example.StudentManagement.storage.FileStorageService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class TeacherService {

    private final TeacherDao dao;
    private final FileStorageService storage;
    private final BCryptPasswordEncoder passwordEncoder;

    public TeacherService(TeacherDao dao, FileStorageService storage, BCryptPasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.storage = storage;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Integer addTeacher(Teacher t, String picName, MultipartFile profilePicture, String password) throws IOException {
        t.setProfilePictureName(null);

        if (password != null && !password.trim().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(password.trim());
            t.setPassword(hashedPassword);
        }

        Integer generatedId = dao.add(t, null);
        t.setId(generatedId);

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String finalName = storage.saveTeacherImage(generatedId, picName, profilePicture);
            t.setProfilePictureName(finalName);
            dao.update(t, null);
        }

        return generatedId;
    }

    @Transactional
    public void updateTeacher(Teacher input, String picName, MultipartFile profilePicture, boolean removePicture, String password) throws IOException {
        Teacher existing = dao.getById(input.getId());

        existing.setName(input.getName());
        existing.setEmail(input.getEmail());
        existing.setCourseId(input.getCourseId());

        if (password != null && !password.trim().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(password.trim()));
        }

        String old = existing.getProfilePictureName();
        if (removePicture) {
            if (old != null) storage.deleteTeacherImage(old);
            existing.setProfilePictureName(null);
        } else if (profilePicture != null && !profilePicture.isEmpty()) {
            if (old != null) storage.deleteTeacherImage(old);
            String finalName = storage.saveTeacherImage(existing.getId(), picName, profilePicture);
            existing.setProfilePictureName(finalName);
        }

        dao.update(existing, null);
    }

    @Transactional
    public void deleteTeacher(int id) {
        String old = dao.getPictureName(id);
        if (old != null) storage.deleteTeacherImage(old);
        dao.delete(id, null);
    }

    @Transactional
    public List<Object[]> getTeachers() {
        return dao.list();
    }

    public Teacher authenticateTeacher(String email, String password) {
        if (email == null || password == null) return null;

        Teacher teacher = dao.getByEmail(email.trim());
        if (teacher == null) return null;

        if (passwordEncoder.matches(password, teacher.getPassword())) {
            teacher.setPassword(null);
            return teacher;
        }
        return null;
    }

    public String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            password.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return password.toString();
    }

    public Teacher getTeacherById(int id) {
        Teacher teacher = dao.getById(id);
        if (teacher != null) {
            teacher.setPassword(null);
        }
        return teacher;
    }

    public List<Object[]> getStudentsByCourse(Integer courseId) {
        return dao.getStudentsByCourseId(courseId);
    }
}
