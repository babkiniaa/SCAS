package org.github.babkiniaa.scas.controllers;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.forUserDto.ChangePasswordDto;
import org.github.babkiniaa.scas.dto.forUserDto.VeritifyEmail;
import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.exception.PasswordException;
import org.github.babkiniaa.scas.service.EmailService;
import org.github.babkiniaa.scas.service.TokenService;
import org.github.babkiniaa.scas.service.UserService;
import org.github.babkiniaa.scas.util.ValidCollerctor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * Контроллер для изменения пароля пользователя.
 * Отвечает за отправку кода подтверждения на email, проверку кода и обновление пароля.
 */
@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://front-img-production.up.railway.app")
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
            @RequestBody @Valid VeritifyEmail email,
            BindingResult result
    ) throws MessagingException, UnsupportedEncodingException {
        Map<String, String> errors = ValidCollerctor.collectValidationErrors(result);

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
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
            @RequestBody @Valid ChangePasswordDto changePasswordDto,
            BindingResult result
    ) {
        Map<String, String> errors = ValidCollerctor.collectValidationErrors(result);
        ValidCollerctor.checkPasswordMatch(changePasswordDto.getPassword(), changePasswordDto.getPasswordConfirm(), errors);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        User user = tokenService.getByVerifyCode(changePasswordDto.getToken()).getUser();
        userService.changePasswordUser(user, changePasswordDto.getPassword());

        return ResponseEntity.ok("Password change");
    }
}
