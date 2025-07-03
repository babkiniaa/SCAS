package org.github.babkiniaa.scas.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.MasterServiceClient;
import org.github.babkiniaa.scas.dto.Response.TaskInQueueDto;
import org.github.babkiniaa.scas.dto.forUserDto.ViewUserForAdmin;
import org.github.babkiniaa.scas.mappers.UserMapper;
import org.github.babkiniaa.scas.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:9000")
@RequiredArgsConstructor
@Tag(name = "Admin Controller", description = "Контроллер для административного управления пользователями и задачами агентов")
public class AdminController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final MasterServiceClient masterServiceClient;

    @Operation(summary = "Получить список всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен")
    @GetMapping("/users")
    public ResponseEntity<List<ViewUserForAdmin>> getAllUsers() {
        List<ViewUserForAdmin> userPage = userMapper.toAdmin(userService.getAllUsers());
        return ResponseEntity.ok(userPage);
    }

    @Operation(summary = "Заблокировать пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно заблокирован"),
            @ApiResponse(responseCode = "400", description = "Неверный идентификатор пользователя")
    })
    @PostMapping("/blockUser")
    public ResponseEntity<String> blockUser(
            @Parameter(description = "ID пользователя для блокировки") @RequestParam("userId") Long userId) {
        userService.blockUser(userId);
        return ResponseEntity.ok("User with ID " + userId + " has been blocked.");
    }

    @Operation(summary = "Разблокировать пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно разблокирован"),
            @ApiResponse(responseCode = "400", description = "Неверный идентификатор пользователя")
    })
    @PostMapping("/unBlockUser")
    public ResponseEntity<String> unBlockUser(
            @Parameter(description = "ID пользователя для разблокировки") @RequestParam("userId") Long userId) {
        userService.unBlockUser(userId);
        return ResponseEntity.ok("User with ID " + userId + " has been unblocked.");
    }

    @Operation(summary = "Получить количество задач в очереди агента")
    @ApiResponse(responseCode = "200", description = "Количество задач успешно получено")
    @GetMapping("/agent/count-queue")
    public int getCountQueue(){
        return masterServiceClient.getCountQueue();
    }

    @Operation(summary = "Получить выполняющиеся задачи агента")
    @ApiResponse(responseCode = "200", description = "Список задач успешно получен")
    @GetMapping("/agent/get-run-task")
    public List<TaskInQueueDto> getRunTask(){
        return masterServiceClient.getRunTask();
    }

    @Operation(summary = "Забанить задачу агента по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задача успешно забанена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @PostMapping("/agent/task/{id}/ban")
    public void banTask(
            @Parameter(description = "ID задачи для блокировки") @PathVariable("id") long taskId){
        masterServiceClient.banTask(taskId);
    }
}
