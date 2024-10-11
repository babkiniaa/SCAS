package com.example.demo.dto;

import com.example.demo.validatros.Password;
import com.example.demo.validatros.UniqueEmail;
import com.example.demo.validatros.UniqueLogin;
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
  private String login;
}
