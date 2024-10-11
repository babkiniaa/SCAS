package org.github.babkiniaa.scas.controllers;

import org.github.babkiniaa.scas.dto.LoginDto;
import org.github.babkiniaa.scas.dto.RegistrationDto;
import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.mappers.UserMapper;
import org.github.babkiniaa.scas.service.EmailService;
import org.github.babkiniaa.scas.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для обработки запросов регистрации, входа в систему и верификации пользователей.
 * Обеспечивает основные функции аутентификации и регистрации пользователей.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:9000")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserMapper userMapper;
    private final EmailService emailService;

    /**
     * Регистрация нового пользователя.
     * Метод принимает данные регистрации, проверяет их на валидность,
     * сравнивает пароли,
     * регистрирует пользователя и отправляет ему письмо для подтверждения аккаунта.
     *
     * @param registrationDto данные для регистрации пользователя.
     * @param result объект для обработки ошибок валидации.
     * @param request объект запроса, необходимый для формирования ссылки для подтверждения.
     * @return ResponseEntity с сообщением об успешной отправке письма для подтверждения или ошибках.
     * @throws UnsupportedEncodingException если возникли ошибки при кодировании текста в email.
     * @throws MessagingException если возникли проблемы с отправкой email.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegistrationDto registrationDto,
            BindingResult result,
            HttpServletRequest request
    ) throws UnsupportedEncodingException, MessagingException {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors occurred.");
        }
        if (!registrationDto.getPassword().equals(registrationDto.getPasswordConfirm())) {
            return ResponseEntity.badRequest().body("Passwords don't match.");
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
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    );
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok("Login successful");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
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
