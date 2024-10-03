package com.example.demo.controllers;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.RegistrationDto;
import com.example.demo.entity.User;
import com.example.demo.exception.PasswordException;
import com.example.demo.mappers.UserMapper;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;


@Controller
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final EmailService emailService;

    @GetMapping("/register")
    public String showReg(Model model, @RequestParam(value = "message", required = false) String message) {
        model.addAttribute("user", new RegistrationDto());
        model.addAttribute("loginForm", new LoginDto());
        model.addAttribute("toggleClass", "sign-up");
        model.addAttribute("message", message);
        return "auth";
    }

    @PostMapping("/register")
    public String registerUser(Model model, @Valid @ModelAttribute("user") RegistrationDto registrationDto, BindingResult result, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException, PasswordException {
        if (result.hasErrors()) {
            model.addAttribute("loginForm", new LoginDto());
            model.addAttribute("toggleClass", "sign-up");
            return "auth";
        }

        if (!registrationDto.getPassword().equals(registrationDto.getPasswordConfirm())) {
            model.addAttribute("errorMessage", "Passwords don't match.");
            model.addAttribute("loginForm", new LoginDto());
            model.addAttribute("toggleClass", "sign-up");
            throw new PasswordException("Passwords don't match.");
        }

        User user = userMapper.toEntity(registrationDto);
        String verificationCode = userService.registerUser(user);
        emailService.sendVerificationEmail(user.getEmail(), verificationCode, request);
        return "redirect:/auth/register?message=Verification email sent to your email address";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginDto());
        model.addAttribute("user", new RegistrationDto());
        model.addAttribute("toggleClass", "sign-in");
        return "auth";
    }


    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code, Model model) {
        if (userService.verify(code)) {
            model.addAttribute("message", "Verification successful. You can now log in.");
            return "redirect:/auth/login";
        } else {
            model.addAttribute("message", "Verification failed.");
            return "redirect:/auth/login";
        }
    }
}