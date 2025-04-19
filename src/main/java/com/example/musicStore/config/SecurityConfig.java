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
                        .requestMatchers("/", "/index.html", "/cart.html", "/login.html", "/register.html", "/css/**", "/js/**", "/images/**", "/api/public/**", "/api/users/register").permitAll()
                        .requestMatchers("/profile.html", "/api/users/me", "/api/users/change-password","/api/cart/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
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
                        .invalidateHttpSession(true) // Убедимся, что сессия завершается при логауте
                        .deleteCookies("JSESSIONID") // Удаляем cookie при логауте
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // Создаем сессию, если требуется
                        .maximumSessions(1)
                        .expiredUrl("/login.html?expired=true")
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