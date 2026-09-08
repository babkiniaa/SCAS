package org.github.babkiniaa.scas.controllers;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class VerifyController {

    private static final Logger log = LoggerFactory.getLogger(VerifyController.class);

    private final UserService userService;

    /**
     * Обрабатывает запрос подтверждения email пользователя по коду.
     * Добавляет в модель сообщение об успехе или ошибке.
     *
     * @param code  код подтверждения из ссылки
     * @param model модель для передачи данных в представление
     * @return имя HTML-шаблона для отображения результата
     */
    @GetMapping("/verify")
    public String verifyUser(@RequestParam("code") String code, Model model) {
        boolean isVerified = userService.verify(code);
        if (isVerified) {
            log.info("User email verified successfully for code {}", code);
            model.addAttribute("message", "Email successfully verified! You can now log in.");
            model.addAttribute("status", "success");
        } else {
            log.warn("Email verification failed for code {}", code);
            model.addAttribute("message", "Email verification failed. Please try again or contact support.");
            model.addAttribute("status", "error");
        }
        return "verifyEmail";
    }
}
