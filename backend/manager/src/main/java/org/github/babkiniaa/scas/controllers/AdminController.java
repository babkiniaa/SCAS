package org.github.babkiniaa.scas.controllers;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.MasterServiceClient;
import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления пользователями в административной панели.
 * Обеспечивает функциональность просмотра всех пользователей и блокировки пользователя.
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private UserService userService;
    private final MasterServiceClient masterServiceClient;

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

    @GetMapping("/agent/count-queue")
    int getCountQueue(){
        return masterServiceClient.getCountQueue();
    }

    @GetMapping("/agent/get-run-task")
    List<Long> getRunTask(){
        return masterServiceClient.getRunTask();
    }

    @PostMapping("/agent/task/ban/{id}")
    void banTask(@PathVariable("id") long taskId){
        masterServiceClient.banTask(taskId);
    }

}