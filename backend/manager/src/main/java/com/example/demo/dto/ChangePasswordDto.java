package com.example.demo.dto;

import com.example.demo.validatros.Password;
import com.example.demo.validatros.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

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
