package com.example.StudentManagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "studenttable")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roll;

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotNull
    private Integer age;

    @NotNull
    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "profile_picture_name")
    private String profilePictureName;

    public Student() {
    }

    public Student(Integer roll, String name, String email, Integer age, Integer courseId) {
        this.roll = roll;
        this.name = name;
        this.email = email;
        this.age = age;
        this.courseId = courseId;
    }

    public Student(Integer roll, String name, String email, Integer age, Integer courseId, String profilePictureName) {
        this.roll = roll;
        this.name = name;
        this.email = email;
        this.age = age;
        this.courseId = courseId;
        this.profilePictureName = profilePictureName;
    }

    public Integer getRoll() {
        return roll;
    }

    public void setRoll(Integer roll) {
        this.roll = roll;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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
