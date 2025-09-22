package com.example.StudentManagement.service;

import com.example.StudentManagement.dao.DocumentDao;
import com.example.StudentManagement.entity.Document;
import com.example.StudentManagement.storage.DocumentStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentService {

    private final DocumentDao dao;
    private final DocumentStorageService storage;

    public DocumentService(DocumentDao dao, DocumentStorageService storage) {
        this.dao = dao;
        this.storage = storage;
    }

    @Transactional
    public void uploadAll(int studentRoll, MultipartFile aadhar, MultipartFile pan, MultipartFile marksheet) throws IOException {
        storage.saveAllPdfs(studentRoll, aadhar, pan, marksheet);
        Document doc = new Document(studentRoll, "aadhar.pdf", "pan.pdf", "marksheet.pdf", true, true, true);
        dao.upsert(doc, null);
    }

    @Transactional
    public void deleteAll(int studentRoll) {
        storage.deleteAll(studentRoll);
        dao.deleteByStudentRoll(studentRoll, null);
    }

    @Transactional
    public List<Object[]> list() {
        return dao.list();
    }
}
