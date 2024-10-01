package com.example.demo.controllers;

import com.example.demo.dto.ChangePasswordDto;
import com.example.demo.dto.RegistrationDto;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import com.example.demo.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class PasswordController {

    private final EmailService emailService;

    private final UserService userService;

    @GetMapping("/change")
    public String showFormChangePassword(Model model){
        model.addAttribute("email", new RegistrationDto());
        return "form-email";
    }

    @PostMapping("/change")
    public String verifyEmailForChangePassword(@ModelAttribute("email") ChangePasswordDto email, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String code = userService.changePassword(email.getEmail());
        emailService.sendVerificationPassword(email.getEmail(), code, request);
        return "login";
    }

    @GetMapping("/change-password")
    public String changePasswordVerify(@Param("code") String code, Model model){
        model.addAttribute("changePassword", new ChangePasswordDto());
        if (userService.verifyForChangePassword(code)) {
            model.addAttribute("verify-code", code);
            return "change-password";
        } else {
            return "login";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("changePassword") ChangePasswordDto changePasswordDto, HttpServletRequest request){
        User user = userService.getByVerifyCode(request.getParameter("verify-code"));
        if(changePasswordDto.getPassword().equals(changePasswordDto.getPasswordConfirm())){
            userService.changePasswordUser(user, changePasswordDto.getPassword());
        }
        return "login";
    }

}
