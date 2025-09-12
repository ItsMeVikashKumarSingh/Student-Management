package com.example.StudentManagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

    public Teacher() {
    }

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
}
