package com.example.demo.dto;

import com.example.demo.validatros.Password;
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
  @NotEmpty(message = "The field is not filled in")
  private String username;
}
