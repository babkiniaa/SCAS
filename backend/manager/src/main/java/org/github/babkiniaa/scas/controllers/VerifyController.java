package org.github.babkiniaa.scas.controllers;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class VerifyController {

    private final UserService userService;

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("code") String code, Model model) {
        boolean isVerified = userService.verify(code);
        if (isVerified) {
            model.addAttribute("message", "Email successfully verified! You can now log in.");
            model.addAttribute("status", "success");
        } else {
            model.addAttribute("message", "Email verification failed. Please try again or contact support.");
            model.addAttribute("status", "error");
        }
        return "verifyEmail";
    }
}
