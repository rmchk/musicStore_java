package com.example.musicStore.controller;

import com.example.musicStore.config.Views;
import com.example.musicStore.model.User;
import com.example.musicStore.service.ReCaptchaService;
import com.example.musicStore.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReCaptchaService reCaptchaService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            // Проверка reCAPTCHA
            if (request.getRecaptchaResponse() == null || !reCaptchaService.verify(request.getRecaptchaResponse())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Ошибка проверки reCAPTCHA");
            }

            // Валидация
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Имя пользователя не может быть пустым");
            }
            if (request.getEmail() == null || !request.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Неверный формат e-mail");
            }
            if (request.getPassword() == null || request.getPassword().length() < 6) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Пароль должен быть не менее 6 символов");
            }

            // Проверяем, существует ли пользователь с таким username
            try {
                userService.loadUserByUsername(request.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Пользователь с таким именем уже существует");
            } catch (UsernameNotFoundException e) {
                // Пользователь не найден, можно регистрировать
            }

            // Создаем объект User
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setRole("USER");

            // Сохраняем пользователя с шифрованием пароля
            User savedUser = userService.saveUserWithPassword(user);
            return ResponseEntity.ok("Пользователь успешно зарегистрирован: " + savedUser.getUsername());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при регистрации: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    @JsonView(Views.Public.class)
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Пользователь не аутентифицирован");
            }
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при получении данных пользователя: " + e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Пользователь не аутентифицирован");
            }
            String username = authentication.getName();
            User user = userService.findByUsername(username);

            // Проверяем, что старый пароль совпадает
            if (!userService.getPasswordEncoder().matches(request.getOldPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Неверный старый пароль");
            }

            // Проверяем, что новый пароль соответствует требованиям
            if (request.getNewPassword() == null || request.getNewPassword().length() < 6) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Новый пароль должен быть не менее 6 символов");
            }

            // Обновляем пароль
            user.setPassword(request.getNewPassword());
            userService.saveUserWithPassword(user);
            return ResponseEntity.ok("Пароль успешно изменен");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при смене пароля: " + e.getMessage());
        }
    }

    @PostMapping("/change-email")
    public ResponseEntity<?> changeEmail(@RequestBody ChangeEmailRequest request, Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Пользователь не аутентифицирован");
            }
            String username = authentication.getName();
            User user = userService.findByUsername(username);

            // Валидация нового email
            if (request.getNewEmail() == null || !request.getNewEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Неверный формат e-mail");
            }

            // Проверяем уникальность email
            if (userService.emailExists(request.getNewEmail()) && !request.getNewEmail().equals(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Этот e-mail уже используется");
            }

            // Обновляем email
            user.setEmail(request.getNewEmail());
            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok("Email успешно изменён");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при смене email: " + e.getMessage());
        }
    }
}

class UserRegistrationRequest {
    private String username;
    private String email;
    private String password;
    private String recaptchaResponse;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRecaptchaResponse() {
        return recaptchaResponse;
    }

    public void setRecaptchaResponse(String recaptchaResponse) {
        this.recaptchaResponse = recaptchaResponse;
    }
}

class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

class ChangeEmailRequest {
    private String newEmail;

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }
}