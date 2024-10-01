package com.example.demo.controllers;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.RegistrationDto;
import com.example.demo.entity.User;
import com.example.demo.mappers.UserMapper;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final EmailService emailService;

    @GetMapping("/register")
    public String showReg(Model model){
        model.addAttribute("user", new RegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(RegistrationDto registrationDto, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        User user = userMapper.toEntity(registrationDto);
        String verificationCode = userService.registerUser(user);
        emailService.sendVerificationEmail(user.getEmail(), verificationCode, request);
        return "login";
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userService.verify(code)) {
            return "login";
        } else {
            return "login";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new LoginDto());
        return "login";
    }
}
