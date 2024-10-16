package org.github.babkiniaa.scas.dto;

import org.github.babkiniaa.scas.validatros.Password;
import org.github.babkiniaa.scas.validatros.UniqueEmail;
import org.github.babkiniaa.scas.validatros.UniqueLogin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) для регистрации нового пользователя.
 * Содержит поля для email, логина, пароля и подтверждения пароля, а также аннотации для валидации.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class RegistrationDto {

    @Email
    @UniqueEmail
    @NotEmpty(message = "The field is not filled in")
    private String email;

    @Password
    @NotEmpty(message = "The field is not filled in")
    private String password;

    @Password
    @NotEmpty(message = "The field is not filled in")
    private String passwordConfirm;

    @UniqueLogin
    @NotEmpty(message = "The field is not filled in")
    private String username;
}
