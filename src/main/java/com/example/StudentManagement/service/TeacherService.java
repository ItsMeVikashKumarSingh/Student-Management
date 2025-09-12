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
    public void updateTeacher(Teacher t, String picName, MultipartFile profilePicture) throws IOException {
        if (profilePicture != null && !profilePicture.isEmpty()) {
            String old = dao.getPictureName(t.getId());
            if (old != null) storage.deleteTeacherImage(old);
            String finalName = storage.saveTeacherImage(t.getId(), picName, profilePicture);
            t.setProfilePictureName(finalName);
        }
        dao.update(t);
    }

    @Transactional
    public void deleteTeacher(int id) {
        String old = dao.getPictureName(id);
        dao.delete(id);
        if (old != null) storage.deleteTeacherImage(old);
    }

    @Transactional
    public List<Object[]> getTeachers() {
        return dao.list();
    }
}
