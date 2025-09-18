package com.example.StudentManagement.controller;

import com.example.StudentManagement.service.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    // Upload all three PDFs in one go (UI enforces sequence before enabling save)
    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadAll(@RequestParam Integer studentRoll,
                            @RequestPart("aadhar") MultipartFile aadhar,
                            @RequestPart("pan") MultipartFile pan,
                            @RequestPart("marksheet") MultipartFile marksheet) throws IOException {
        service.uploadAll(studentRoll, aadhar, pan, marksheet);
        return "Documents saved successfully!";
    }

    @GetMapping("/all")
    public List<Object[]> all() {
        return service.list();
    }

    @DeleteMapping("/delete/{studentRoll}")
    public String deleteAll(@PathVariable int studentRoll) {
        service.deleteAll(studentRoll);
        return "Documents deleted";
    }
}
