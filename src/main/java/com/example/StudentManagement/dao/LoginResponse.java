package com.example.StudentManagement.dao;

import com.example.StudentManagement.entity.Teacher;

public class LoginResponse {
    private boolean success;
    private String message;
    private Teacher teacher;

    public LoginResponse() {}

    public LoginResponse(boolean success, String message, Teacher teacher) {
        this.success = success;
        this.message = message;
        this.teacher = teacher;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
