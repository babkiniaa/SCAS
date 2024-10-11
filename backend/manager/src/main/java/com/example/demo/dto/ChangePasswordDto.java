package com.example.demo.dto;

import com.example.demo.validatros.Password;
import com.example.demo.validatros.UniqueEmail;
import jakarta.validation.constraints.Email;
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
  @UniqueEmail
  @Email
  @NotEmpty(message = "The field is not filled in")
  private String email;
  @Password
  @NotEmpty(message = "The field is not filled in")
  private String password;
  @Password
  @NotEmpty(message = "The field is not filled in")
  private String passwordConfirm;
}
