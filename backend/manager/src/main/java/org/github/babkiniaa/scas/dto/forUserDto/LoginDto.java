package org.github.babkiniaa.scas.dto.forUserDto;

import jakarta.validation.constraints.Email;
import org.github.babkiniaa.scas.validatros.Password;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) для аутентификации (входа) пользователя.
 * Содержит поля для логина (username) и пароля, а также аннотации для валидации.
 */
@Setter
@Getter
@RequiredArgsConstructor
public class LoginDto {
    @Password
    @NotEmpty(message = "The field is not filled in")
    private String password;

    @Email
    @NotEmpty(message = "The field is not filled in")
    private String username;
}
