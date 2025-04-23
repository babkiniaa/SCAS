package org.github.babkiniaa.scas.controllers;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.MasterServiceClient;
import org.github.babkiniaa.scas.dto.Response.TaskInQueueDto;
import org.github.babkiniaa.scas.dto.forUserDto.ViewUserForAdmin;
import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.mappers.UserMapper;
import org.github.babkiniaa.scas.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления пользователями в административной панели.
 * Обеспечивает функциональность просмотра всех пользователей и блокировки пользователя.
 */
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:9000")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final MasterServiceClient masterServiceClient;

    /**
     * Получает список всех пользователей с постраничной разбивкой.
     * Добавляет список пользователей и информацию о страницах в модель.
     *

     * @return имя шаблона для отображения списка пользователей.
     */
    @GetMapping("/users")
    public ResponseEntity<List<ViewUserForAdmin>> getAllUsers() {
        List<ViewUserForAdmin> userPage = userMapper.toAdmin(userService.getAllUsers());
        return ResponseEntity.ok(userPage);
    }

    /**
     * Блокирует пользователя по его ID.
     *
     * @param userId ID пользователя, которого необходимо заблокировать.
     * @return статус выполнения.
     */
    @PostMapping("/blockUser")
    public ResponseEntity<String> blockUser(@RequestParam("userId") Long userId) {
        userService.blockUser(userId);
        return ResponseEntity.ok("User with ID " + userId + " has been blocked.");
    }

    @PostMapping("/unBlockUser")
    public ResponseEntity<String> unBlockUser(@RequestParam("userId") Long userId) {
        userService.unBlockUser(userId);
        return ResponseEntity.ok("User with ID " + userId + " has been unblocked.");
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