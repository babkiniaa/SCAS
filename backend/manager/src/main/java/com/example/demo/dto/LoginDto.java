package com.example.demo.dto;

import com.example.demo.validatros.Password;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class LoginDto {
    @Password
    @NotEmpty(message = "The field is not filled in")
    private String password;
    @NotEmpty(message = "The field is not filled in")
    private String loginOrEmail;
}
