package org.github.babkiniaa.scas.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
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
@CrossOrigin(origins = "http://localhost:9000")
public class PasswordController {

    private final EmailService emailService;
    private final UserService userService;
    private final TokenService tokenService;

    @Operation(
            summary = "Отправить код подтверждения на email для смены пароля",
            description = "Принимает email, отправляет код подтверждения для дальнейшей смены пароля"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Код подтверждения отправлен"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера при отправке письма")
    })
    @PostMapping("/change")
    public ResponseEntity<?> verifyEmailForChangePassword(
            @RequestBody(description = "DTO с email для смены пароля", required = true,
                    content = @Content(schema = @Schema(implementation = VeritifyEmail.class)))
            @Valid @org.springframework.web.bind.annotation.RequestBody VeritifyEmail email,
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

    @Operation(
            summary = "Сменить пароль пользователя",
            description = "Проверяет совпадение паролей и обновляет пароль пользователя по токену подтверждения"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пароль успешно изменён"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или несовпадение паролей",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Токен не найден или просрочен")
    })
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody(description = "DTO с новым паролем и токеном подтверждения", required = true,
                    content = @Content(schema = @Schema(implementation = ChangePasswordDto.class)))
            @Valid @org.springframework.web.bind.annotation.RequestBody ChangePasswordDto changePasswordDto,
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
