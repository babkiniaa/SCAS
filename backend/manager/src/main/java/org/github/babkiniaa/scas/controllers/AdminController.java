package org.github.babkiniaa.scas.controllers;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.MasterServiceClient;
import org.github.babkiniaa.scas.dto.Response.TaskInQueueDto;
import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления пользователями в административной панели.
 * Обеспечивает функциональность просмотра всех пользователей и блокировки пользователя.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private UserService userService;
    private final MasterServiceClient masterServiceClient;

    /**
     * Получает список всех пользователей с постраничной разбивкой.
     * Добавляет список пользователей и информацию о страницах в модель.
     *
     * @param model объект для передачи данных в представление.
     * @param page  номер текущей страницы (по умолчанию 0).
     * @param size  количество пользователей на странице (по умолчанию 10).
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

    /**
     * Возвращает количество активных задач с агента
     *
     * @return количество тасок
     */
    @GetMapping("/agent/count-queue")
    public int getCountQueue(){
        return masterServiceClient.getCountQueue();
    }

    /**
     * Возвращает Id тасок, которые непосредственно выполняются на агенте
     *
     * @return the list
     */
    @GetMapping("/agent/get-run-task")
    public List<TaskInQueueDto> getRunTask(){
        return masterServiceClient.getRunTask();
    }

    /**
     * Ban Таски, если она не начала выполняться, статус ban не даст ей начать выполнение
     *
     * @param taskId Id таски для ban
     */
    @PostMapping("/agent/task/ban/{id}")
    public void banTask(@PathVariable("id") long taskId){
        masterServiceClient.banTask(taskId);
    }

}