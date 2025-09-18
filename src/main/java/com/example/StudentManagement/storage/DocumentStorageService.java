package com.example.StudentManagement.storage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Locale;

@Service
public class DocumentStorageService {

    private static final Path ROOT = Paths.get("uploads/image/docs");
    private static final long MAX_BYTES = 5L * 1024 * 1024; // 5MB

    public DocumentStorageService() {
        try {
            Files.createDirectories(ROOT);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create docs root", e);
        }
    }

    public void saveAllPdfs(int studentRoll, MultipartFile aadhar, MultipartFile pan, MultipartFile marksheet) throws IOException {
        validatePdf(aadhar);
        validatePdf(pan);
        validatePdf(marksheet);
        Path dir = dirFor(studentRoll);
        Files.createDirectories(dir);
        Files.copy(aadhar.getInputStream(), dir.resolve("aadhar.pdf"), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(pan.getInputStream(), dir.resolve("pan.pdf"), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(marksheet.getInputStream(), dir.resolve("marksheet.pdf"), StandardCopyOption.REPLACE_EXISTING);
    }

    public void saveSinglePdf(int studentRoll, String type, MultipartFile file) throws IOException {
        validatePdf(file);
        Path dir = dirFor(studentRoll);
        Files.createDirectories(dir);
        Files.copy(file.getInputStream(), dir.resolve(safeName(type)), StandardCopyOption.REPLACE_EXISTING);
    }

    public void deleteAll(int studentRoll) {
        Path dir = dirFor(studentRoll);
        try (DirectoryStream<Path> ds = Files.isDirectory(dir) ? Files.newDirectoryStream(dir) : null) {
            if (ds != null) {
                for (Path p : ds) Files.deleteIfExists(p);
                Files.deleteIfExists(dir);
            }
        } catch (IOException ignored) {}
    }

    public void deleteSingle(int studentRoll, String type) {
        Path dir = dirFor(studentRoll);
        try {
            Files.deleteIfExists(dir.resolve(safeName(type)));
        } catch (IOException ignored) {}
    }

    private void validatePdf(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("PDF is empty");
        if (file.getSize() > MAX_BYTES) throw new IllegalArgumentException("PDF must be ≤ 5MB");
        String ct = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        if (!ct.equals("application/pdf")) throw new IllegalArgumentException("Only PDF is allowed");
    }

    private Path dirFor(int studentRoll) {
        return ROOT.resolve(String.valueOf(studentRoll));
    }

    private String safeName(String type) {
        switch (type.toLowerCase(Locale.ROOT)) {
            case "aadhar": return "aadhar.pdf";
            case "pan": return "pan.pdf";
            case "marksheet": return "marksheet.pdf";
            default: throw new IllegalArgumentException("Unknown document type");
        }
    }
}
