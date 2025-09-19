package com.example.StudentManagement.config;

import com.example.StudentManagement.service.TeacherUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
                        // Public resources
                        .requestMatchers("/", "/error", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/image/**", "/docs/**").permitAll()

                        // Authentication endpoints
                        .requestMatchers("/login.html", "/auth/authenticate").permitAll()
                        .requestMatchers("/teacher-dashboard.html").permitAll()

                        // Protected endpoints
                        .requestMatchers("/dashboard.html", "/students.html", "/teachers.html", "/courses.html").authenticated()
                        .requestMatchers("/api/current-role").authenticated()
                        .requestMatchers("/students/all", "/teachers/all", "/courses/all", "/courses/suggestions").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/students/add", "/students/update", "/students/delete/**",
                                "/teachers/add", "/teachers/update", "/teachers/delete/**",
                                "/courses/add", "/courses/update", "/courses/delete/**").hasRole("ADMIN")
                        .requestMatchers("/teachers/generate-password").hasRole("ADMIN")
                        .requestMatchers("/teachers/**").authenticated()  // Teacher endpoints

                        .anyRequest().authenticated()
                )
                // **SESSION MANAGEMENT: Handle expired/invalid sessions**
                .sessionManagement(session -> session
                        .invalidSessionUrl("/login.html?expired=true")  // Redirect when session is invalid
                        .maximumSessions(1)  // Allow only 1 session per user
                        .maxSessionsPreventsLogin(false)  // Don't prevent new login, expire old session
                        .expiredUrl("/login.html?expired=true")  // Redirect when session expires due to concurrent login
                )
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults())

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login.html")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .authenticationProvider(inMemoryAuthenticationProvider())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider inMemoryAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(inMemoryUserDetailsService());
        provider.setPasswordEncoder(noOpPasswordEncoder());
        return provider;
    }

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @SuppressWarnings("deprecation")
    public NoOpPasswordEncoder noOpPasswordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsService inMemoryUserDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password("password")
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
