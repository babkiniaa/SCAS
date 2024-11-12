package org.github.babkiniaa.scas.dto.forUserDto;

import org.github.babkiniaa.scas.validatros.Password;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) для изменения пароля пользователя.
 * Содержит поля для email, нового пароля и подтверждения пароля,
 * а также необходимые аннотации для валидации.
 */
@Getter
@Setter
public class ChangePasswordDto {

    @Password
    @NotEmpty(message = "The field is not filled in")
    private String password;

    @Password
    @NotEmpty(message = "The field is not filled in")
    private String passwordConfirm;

    @NotEmpty(message = "The field is not filled in")
    private String token;
}
