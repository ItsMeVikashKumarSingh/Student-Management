package com.example.StudentManagement.config;

import com.example.StudentManagement.service.TeacherUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TeacherUserDetailsService teacherUserDetailsService;

    public SecurityConfig(TeacherUserDetailsService teacherUserDetailsService) {
        this.teacherUserDetailsService = teacherUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/error", "/static/**", "/login**", "/css/**", "/js/**","/images/", "/css/").permitAll()
                        .requestMatchers("/image/**").permitAll()
                        .requestMatchers("/docs/**").permitAll()
                        .requestMatchers("/teacher-login.html", "/teacher-dashboard.html").permitAll()
                        .requestMatchers("/teachers/login", "/teachers/logout", "/teachers/dashboard", "/teachers/session", "/teachers/generate-password").permitAll()
                        .requestMatchers("/dashboard.html", "/students.html", "/teachers.html", "/courses.html", "/documents.html").authenticated()
                        .requestMatchers("/api/current-role").authenticated()
                        .requestMatchers("/students/all", "/teachers/all", "/courses/all","/courses/suggestions").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/students/add", "/students/update", "/students/delete/**",
                                "/teachers/add", "/teachers/update", "/teachers/delete/**",
                                "/courses/add", "/courses/update", "/courses/delete/**").hasRole("ADMIN")
                        .requestMatchers("/documents/all",
                                "/documents/upload",
                                "/documents/delete/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .successHandler(customSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                )
                .authenticationProvider(inMemoryAuthenticationProvider())
                .authenticationProvider(teacherAuthenticationProvider())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                String redirectUrl = "/dashboard.html";
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

                // Check if it's a teacher login
                if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"))) {
                    redirectUrl = "/teacher-dashboard.html";
                } else {
                    for (GrantedAuthority authority : authorities) {
                        if (authority.getAuthority().equals("ROLE_ADMIN")) {
                            redirectUrl = "/dashboard.html";
                            break;
                        } else if (authority.getAuthority().equals("ROLE_USER")) {
                            redirectUrl = "/dashboard.html";
                            break;
                        }
                    }
                }
                super.setDefaultTargetUrl(redirectUrl);
                super.onAuthenticationSuccess(request, response, authentication);
            }
        };
    }

    // Separate authentication providers
    @Bean
    public AuthenticationProvider inMemoryAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(inMemoryUserDetailsService());
        provider.setPasswordEncoder(noOpPasswordEncoder()); // Use NoOp for in-memory users
        return provider;
    }

    @Bean
    public AuthenticationProvider teacherAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(teacherUserDetailsService);
        provider.setPasswordEncoder(bcryptPasswordEncoder()); // Use BCrypt for teachers
        return provider;
    }

    // Password encoders
    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public NoOpPasswordEncoder noOpPasswordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    // Main password encoder (delegating)
    @Bean
    public PasswordEncoder passwordEncoder() {
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());

        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(NoOpPasswordEncoder.getInstance());

        return passwordEncoder;
    }

    @Bean
    public UserDetailsService inMemoryUserDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password("password") // Plain text for NoOp encoder
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password("password") // Plain text for NoOp encoder
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
