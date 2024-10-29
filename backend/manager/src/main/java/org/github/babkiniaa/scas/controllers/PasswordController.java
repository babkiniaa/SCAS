package org.github.babkiniaa.scas.controllers;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.ChangePasswordDto;
import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.exception.PasswordException;
import org.github.babkiniaa.scas.service.EmailService;
import org.github.babkiniaa.scas.service.TokenService;
import org.github.babkiniaa.scas.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.UnsupportedEncodingException;


/**
 * Контроллер для изменения пароля пользователя.
 * Отвечает за отправку кода подтверждения на email, проверку кода и обновление пароля.
 */
@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:9000")
public class PasswordController {

    private final EmailService emailService;

    private final UserService userService;

    private final TokenService tokenService;


    /**
     * Обрабатывает запрос на смену пароля отправляет письмо с кодом подтверждения на указанный email.
     *
     * @param email DTO с email для смены пароля
     * @return имя шаблона для страницы авторизации
     * @throws MessagingException           при ошибке отправки письма
     * @throws UnsupportedEncodingException при ошибке кодировки email
     */
    @PostMapping("/change")
    public ResponseEntity<?> verifyEmailForChangePassword(
            @RequestBody ChangePasswordDto email
    ) throws MessagingException, UnsupportedEncodingException {
        String code = userService.changePassword(email.getEmail());
        emailService.sendVerificationPassword(email.getEmail(), code);
        return ResponseEntity.ok("The token was sent to confirm the mail");
    }

    /**
     * Обрабатывает смену пароля проверяет совпадение введенных паролей обновляяя пароль пользователя.
     *
     * @param changePasswordDto DTO с новыми паролями
     * @return имя шаблона для страницы авторизации
     * @throws PasswordException если пароли не совпадают
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordDto changePasswordDto
    ) throws PasswordException {
        User user = tokenService.getByVerifyCode(changePasswordDto.getToken()).getUser();
        if (changePasswordDto.getPassword().equals(changePasswordDto.getPasswordConfirm())) {
            userService.changePasswordUser(user, changePasswordDto.getPassword());
        } else {
            throw new PasswordException("passwords don't match");
        }
        return ResponseEntity.ok("Password change");
    }
}
