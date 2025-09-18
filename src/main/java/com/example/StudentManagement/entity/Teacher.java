package com.example.StudentManagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "teachertable")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotNull
    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "profile_picture_name")
    private String profilePictureName;

    // NEW: Password field with JPA and Jackson annotations
    @Column(name = "password")
    private String password;

    // Transient field for course name (not stored in DB)
    @Transient
    private String courseName;

    // Default constructor
    public Teacher() {
    }

    // Existing constructors
    public Teacher(Integer id, String name, String email, Integer courseId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.courseId = courseId;
    }

    public Teacher(Integer id, String name, String email, Integer courseId, String profilePictureName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.courseId = courseId;
        this.profilePictureName = profilePictureName;
    }

    // NEW: Constructor with password
    public Teacher(Integer id, String name, String email, Integer courseId, String profilePictureName, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.courseId = courseId;
        this.profilePictureName = profilePictureName;
        this.password = password;
    }

    // Existing getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getProfilePictureName() {
        return profilePictureName;
    }

    public void setProfilePictureName(String profilePictureName) {
        this.profilePictureName = profilePictureName;
    }

    // NEW: Password getters and setters with security annotations
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Course name getter/setter (transient field)
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
