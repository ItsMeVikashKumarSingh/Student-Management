package com.example.StudentManagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "studenttable")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roll;

    private String name;
    private String email;
    private Integer age;
    private String course;

    public Student() {}

    public Student(Integer roll, String name, String email, Integer age, String course) {
        this.roll = roll;
        this.name = name;
        this.email = email;
        this.age = age;
        this.course = course;
    }

    // getters & setters

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
    public String getCourse() {
        return course;
    }
    public void setCourse(String course) {
        this.course = course;
    }
}
