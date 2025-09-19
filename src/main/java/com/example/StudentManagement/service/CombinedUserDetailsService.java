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
public class CombinedUserDetailsService implements UserDetailsService {  // Renamed for clarity

    private final TeacherDao teacherDao;

    public CombinedUserDetailsService(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Handle in-memory admin/user with BCrypt-hashed passwords
        if ("admin".equals(username)) {
            return User.builder()
                    .username("admin")
                    .password("$2a$10$A4PSD0eLj/aGc8POIuSH.ue9HdXIc3yBN4nABKjaafE91Tz/P.3te")  // Your BCrypt hash
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                    .build();
        } else if ("user".equals(username)) {
            return User.builder()
                    .username("user")
                    .password("$2a$10$A4PSD0eLj/aGc8POIuSH.ue9HdXIc3yBN4nABKjaafE91Tz/P.3te")  // Your BCrypt hash
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                    .build();
        } else {
            // Handle teachers (unchanged, assumes password is BCrypt-encoded in DB)
            Teacher teacher = teacherDao.getByEmail(username);

            if (teacher == null) {
                throw new UsernameNotFoundException("Teacher not found with email: " + username);
            }

            return User.builder()
                    .username(teacher.getEmail())
                    .password(teacher.getPassword()) // Already BCrypt encoded
                    .authorities(List.of(new SimpleGrantedAuthority("ROLE_TEACHER")))
                    .build();
        }
    }
}
