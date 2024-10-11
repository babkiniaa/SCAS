package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для обработки запросов, связанных с пользователями.
 * Обеспечивает маршрутизацию на домашнюю страницу.
 */
@Controller
public class UserController {

  @GetMapping("home")
  public String home() {
    return "home";
  }
}
