package org.github.babkiniaa.scas.controllers;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.forUserDto.LoginDto;
import org.github.babkiniaa.scas.dto.forUserDto.RegistrationDto;
import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.exception.NotFoundUserException;
import org.github.babkiniaa.scas.mappers.UserMapper;
import org.github.babkiniaa.scas.service.AuthService;
import org.github.babkiniaa.scas.service.EmailService;
import org.github.babkiniaa.scas.service.UserService;
import org.github.babkiniaa.scas.util.ValidCollerctor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Контроллер для обработки запросов регистрации, входа в систему и верификации пользователей.
 * Обеспечивает основные функции аутентификации и регистрации пользователей.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:9000")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final EmailService emailService;

    /**
     * Регистрирует нового пользователя, проверяет данные на валидность,
     * отправляет письмо с подтверждением регистрации.
     *
     * @param registrationDto DTO с данными для регистрации пользователя.
     * @param result          Объект BindingResult для хранения ошибок валидации.
     * @param request         Объект HttpServletRequest для получения информации о запросе.
     * @return Ответ с HTTP статусом 200 и сообщением о успешной регистрации,
     * либо 400 с ошибками валидации, если данные неверны.
     * @throws UnsupportedEncodingException если кодировка URL недопустима.
     * @throws MessagingException           если возникает ошибка при отправке электронной почты.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegistrationDto registrationDto,
            BindingResult result,
            HttpServletRequest request
    ) throws UnsupportedEncodingException, MessagingException {
        Map<String, String> errors = ValidCollerctor.collectValidationErrors(result);
        ValidCollerctor.checkPasswordMatch(registrationDto.getPassword(), registrationDto.getPasswordConfirm(), errors);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        User user = userMapper.toEntity(registrationDto);
        String verificationCode = userService.registerUser(user);
        emailService.sendVerificationEmail(user.getEmail(), verificationCode, request);

        return ResponseEntity.ok("Verification email sent to your email address");
    }

    /**
     * Логин пользователя. Метод аутентифицирует пользователя по его логину и паролю.
     * При успешной аутентификации устанавливается контекст безопасности для текущей сессии.
     *
     * @param loginDto объект с данными для аутентификации (логин и пароль).
     * @return ResponseEntity с сообщением об успешной аутентификации или ошибке.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto, BindingResult result) throws NotFoundUserException {
        Map<String, String> errors = ValidCollerctor.collectValidationErrors(result);

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        return ResponseEntity.ok(authService.login(loginDto));
    }

    /**
     * Верификация пользователя. Метод принимает код подтверждения из email и проверяет его.
     * При успешной верификации пользователь может войти в систему.
     *
     * @param code код подтверждения, отправленный на email пользователя.
     * @return ResponseEntity с сообщением об успешной верификации или ошибке.
     */
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("code") String code) {
        if (userService.verify(code)) {
            return ResponseEntity.ok("Verification successful. You can now log in.");
        } else {
            return ResponseEntity.badRequest().body("Verification failed.");
        }
    }
}
