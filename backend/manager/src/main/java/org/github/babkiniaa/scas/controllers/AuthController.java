package org.github.babkiniaa.scas.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
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
import org.springframework.http.MediaType;
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

    @Operation(
            summary = "Регистрация пользователя",
            description = "Создает нового пользователя, проверяет данные и отправляет письмо с подтверждением."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(type = "string"))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Map.class)))
    })
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для регистрации пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegistrationDto.class))
            )
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

        return ResponseEntity.status(201).body("Verification email sent to your email address");
    }

    @Operation(
            summary = "Авторизация пользователя",
            description = "Аутентифицирует пользователя и возвращает результат входа."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный вход",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для аутентификации (логин и пароль)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginDto.class))
            )
            @Valid @RequestBody LoginDto loginDto,
            BindingResult result) throws NotFoundUserException {

        Map<String, String> errors = ValidCollerctor.collectValidationErrors(result);

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        return ResponseEntity.ok(authService.login(loginDto));
    }

}
