package com.example.musicStore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/index.html", "/cart.html", "/login.html", "/register.html", "/verify-registration.html", "/css/**", "/js/**", "/images/**", "/api/public/**", "/api/users/register", "/api/users/verify-registration").permitAll()
                        .requestMatchers("/profile.html", "/api/users/me", "/api/users/change-password", "/api/cart/**").authenticated()
                        .requestMatchers("/admin/**", "/admin.html").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .permitAll()
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/index.html?success=true", true)
                        .failureUrl("/login.html?error=true")
                )
                .logout(logout -> logout
                        .permitAll()
                        .logoutSuccessUrl("/login.html?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .maximumSessions(1)
                        .expiredUrl("/login.html?expired=true")
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/index.html");
                        })
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/images/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}