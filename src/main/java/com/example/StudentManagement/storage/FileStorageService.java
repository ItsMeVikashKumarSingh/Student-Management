package com.example.StudentManagement.storage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Locale;

@Service
public class FileStorageService {

    private static final Path ROOT = Paths.get("uploads");
    private static final Path STUDENT_DIR = ROOT.resolve("image/StudentPic");
    private static final Path TEACHER_DIR = ROOT.resolve("image/TeacherPic");
    private static final long MAX_BYTES = 5L * 1024 * 1024; // 5MB

    public FileStorageService() {
        try {
            Files.createDirectories(STUDENT_DIR);
            Files.createDirectories(TEACHER_DIR);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create upload directories", e);
        }
    }

    public String saveStudentImage(Integer id, String picName, MultipartFile file) throws IOException {
        validateImage(file);
        String ext = extensionFor(file);
        String safeBase = sanitizeBaseName(picName);
        String finalName = id + "_" + safeBase + "." + ext;
        Files.copy(file.getInputStream(), STUDENT_DIR.resolve(finalName), StandardCopyOption.REPLACE_EXISTING);
        return finalName;
    }

    public String saveTeacherImage(Integer id, String picName, MultipartFile file) throws IOException {
        validateImage(file);
        String ext = extensionFor(file);
        String safeBase = sanitizeBaseName(picName);
        String finalName = id + "_" + safeBase + "." + ext;
        Files.copy(file.getInputStream(), TEACHER_DIR.resolve(finalName), StandardCopyOption.REPLACE_EXISTING);
        return finalName;
    }

    public void deleteStudentImage(String fileName) {
        if (fileName == null || fileName.isBlank()) return;
        try {
            Files.deleteIfExists(STUDENT_DIR.resolve(fileName));
        } catch (IOException ignored) {}
    }

    public void deleteTeacherImage(String fileName) {
        if (fileName == null || fileName.isBlank()) return;
        try {
            Files.deleteIfExists(TEACHER_DIR.resolve(fileName));
        } catch (IOException ignored) {}
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Profile picture is empty");
        if (file.getSize() > MAX_BYTES) throw new IllegalArgumentException("Profile picture must be ≤ 5MB");
        String ct = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        if (!(ct.equals("image/jpeg") || ct.equals("image/jpg") || ct.equals("image/png"))) {
            throw new IllegalArgumentException("Only JPG and PNG are allowed");
        }
    }

    private String extensionFor(MultipartFile file) {
        String ct = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        if (ct.contains("png")) return "png";
        return "jpg";
    }

    private String sanitizeBaseName(String base) {
        if (base == null) base = "profile";
        String cleaned = base.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9-_]+", "-");
        if (cleaned.isBlank()) cleaned = "profile";
        return cleaned;
    }
}
