package com.example.demo.controllers;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер для управления пользователями в административной панели.
 * Обеспечивает функциональность просмотра всех пользователей и блокировки пользователя.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

  @Autowired
  private UserService userService;

  /**
   * Получает список всех пользователей с постраничной разбивкой.
   * Добавляет список пользователей и информацию о страницах в модель.
   *
   * @param model объект для передачи данных в представление.
   * @param page номер текущей страницы (по умолчанию 0).
   * @param size количество пользователей на странице (по умолчанию 10).
   * @return имя шаблона для отображения списка пользователей.
   */
  @GetMapping("/users")
  public String getAllUsers(
          Model model, @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size);
    Page<User> userPage = userService.getAllUsers(pageable);
    model.addAttribute("users", userPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", userPage.getTotalPages());
    return "admin/user-list";
  }

  /**
   * Блокирует пользователя по его ID.
   * После блокировки пользователя выполняется перенаправление на страницу со списком пользователей.
   *
   * @param userId ID пользователя, которого необходимо заблокировать.
   * @return редирект на страницу со списком пользователей.
   */
  @PostMapping("/blockUser")
  public String blockUser(@RequestParam("userId") Long userId) {
    userService.blockUser(userId);
    return "redirect:/admin/users";
  }
}