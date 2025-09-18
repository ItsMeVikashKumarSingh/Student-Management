package com.example.StudentManagement.service;

import com.example.StudentManagement.entity.Teacher;
import com.example.StudentManagement.dao.TeacherDao;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherUserDetailsService implements UserDetailsService {

    private final TeacherDao teacherDao;

    public TeacherUserDetailsService(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Teacher teacher = teacherDao.getByEmail(email);

        if (teacher == null) {
            throw new UsernameNotFoundException("Teacher not found with email: " + email);
        }

        return User.builder()
                .username(teacher.getEmail())
                .password(teacher.getPassword()) // Already BCrypt encoded
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_TEACHER")))
                .build();
    }
}
