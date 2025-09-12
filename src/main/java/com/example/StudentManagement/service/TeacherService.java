package com.example.StudentManagement.service;

import com.example.StudentManagement.dao.TeacherDao;
import com.example.StudentManagement.entity.Teacher;
import com.example.StudentManagement.storage.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class TeacherService {

    private final TeacherDao dao;
    private final FileStorageService storage;

    public TeacherService(TeacherDao dao, FileStorageService storage) {
        this.dao = dao;
        this.storage = storage;
    }

    @Transactional
    public Integer addTeacher(Teacher t, String picName, MultipartFile profilePicture) throws IOException {
        t.setProfilePictureName(null);
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
    public void updateTeacher(Teacher input, String picName, MultipartFile profilePicture, boolean removePicture) throws IOException {
        Teacher existing = dao.getById(input.getId());

        existing.setName(input.getName());
        existing.setEmail(input.getEmail());
        existing.setCourseId(input.getCourseId());

        String old = existing.getProfilePictureName();

        if (removePicture) {
            if (old != null) storage.deleteTeacherImage(old);
            existing.setProfilePictureName(null); // UI shows static default
        } else if (profilePicture != null && !profilePicture.isEmpty()) {
            if (old != null) storage.deleteTeacherImage(old);
            String finalName = storage.saveTeacherImage(existing.getId(), picName, profilePicture);
            existing.setProfilePictureName(finalName);
        }
        // else keep old

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
}
