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

    // NEW WAY (Use this):
    public TeacherService(TeacherDao dao, FileStorageService storage, BCryptPasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.storage = storage;
        this.passwordEncoder = passwordEncoder; // Inject via constructor
    }

    @Transactional
    public Integer addTeacher(Teacher t, String picName, MultipartFile profilePicture, String password) throws IOException {
        // DEBUG: Log received parameters
        System.out.println("=== TEACHER SERVICE DEBUG ===");
        System.out.println("Teacher: " + t.getName() + " / " + t.getEmail());
        System.out.println("Password parameter: " + (password != null ? "RECEIVED (length=" + password.length() + ")" : "NULL"));

        t.setProfilePictureName(null);

        // Hash password before saving
        if (password != null && !password.trim().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(password.trim());
            t.setPassword(hashedPassword);
            System.out.println("Password hashed and set on Teacher object");
        } else {
            System.out.println("WARNING: Password is null or empty - this will cause database error!");
        }

        // DEBUG: Log Teacher object before DAO call
        System.out.println("Calling DAO with Teacher object - Password field: " + (t.getPassword() != null ? "SET" : "NULL"));

        Integer id = dao.add(t);
        t.setId(id);

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String finalName = storage.saveTeacherImage(id, picName, profilePicture);
            t.setProfilePictureName(finalName);
            dao.update(t);
        }
        return id;
    }

    @Transactional
    public void updateTeacher(Teacher input, String picName, MultipartFile profilePicture, boolean removePicture, String password) throws IOException {
        Teacher existing = dao.getById(input.getId());

        existing.setName(input.getName());
        existing.setEmail(input.getEmail());
        existing.setCourseId(input.getCourseId());

        // Update password only if provided (for updates)
        if (password != null && !password.trim().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(password.trim()));
        }

        // Handle profile picture logic...
        String old = existing.getProfilePictureName();
        if (removePicture) {
            if (old != null) storage.deleteTeacherImage(old);
            existing.setProfilePictureName(null);
        } else if (profilePicture != null && !profilePicture.isEmpty()) {
            if (old != null) storage.deleteTeacherImage(old);
            String finalName = storage.saveTeacherImage(existing.getId(), picName, profilePicture);
            existing.setProfilePictureName(finalName);
        }

        dao.update(existing);
    }


    @Transactional
    public void deleteTeacher(int id) {
        String old = dao.getPictureName(id);
        if (old != null) storage.deleteTeacherImage(old);
        dao.delete(id);
    }

    @Transactional
    public List<Object[]> getTeachers() {
        return dao.list();
    }

    // NEW: Authentication methods (following your pattern)
    public Teacher authenticateTeacher(String email, String password) {
        if (email == null || password == null) return null;

        Teacher teacher = dao.getByEmail(email.trim());
        if (teacher == null) return null;

        if (passwordEncoder.matches(password, teacher.getPassword())) {
            teacher.setPassword(null); // Remove password from response
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
            teacher.setPassword(null); // Remove password from response
        }
        return teacher;
    }

    public List<Object[]> getStudentsByCourse(Integer courseId) {
        return dao.getStudentsByCourseId(courseId);
    }
}
