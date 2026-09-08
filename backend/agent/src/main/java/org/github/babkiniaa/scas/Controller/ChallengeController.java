package org.github.babkiniaa.scas.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.Request.RegisterTaskDto;
import org.github.babkiniaa.scas.dto.Response.TaskInQueueDto;
import org.github.babkiniaa.scas.dto.StatusDto;
import org.github.babkiniaa.scas.entity.StatusTask;
import org.github.babkiniaa.scas.entity.Task;
import org.github.babkiniaa.scas.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analysis")
@Tag(name = "Challenge Controller", description = "Управление задачами анализа")
public class ChallengeController {

    private final TaskService taskService;

    @PostMapping("/start/queue")
    @Operation(
            summary = "Запустить задачу анализа",
            description = "Создает новую задачу анализа и ставит ее в очередь"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно создана и добавлена в очередь")
    })
    public Long initTask(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для регистрации задачи анализа")
            @RequestBody RegisterTaskDto registerTaskDto
    ) {
        return taskService.saveTask(registerTaskDto);
    }

    @GetMapping("/get-hashmap")
    @Operation(
            summary = "Получить карту анализаторов",
            description = "Возвращает карту доступных анализаторов по категориям"
    )
    @ApiResponse(responseCode = "200", description = "Карта успешно получена")
    public HashMap<String, List<String>> getMethodMap() {
        return taskService.getMethodMap();
    }

    @GetMapping("/task/{id}/status")
    @Operation(
            summary = "Получить статус задачи",
            description = "Возвращает статус задачи анализа по ID проекта"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус успешно получен"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public StatusDto getStatus(
            @Parameter(description = "ID проекта", example = "1")
            @PathVariable("id") long projectId
    ) {
        try {
            Task task = taskService.getStatusByProjectId(projectId);
            return new StatusDto(task.getStatusTask().toString(), task.getMessage());
        } catch (Exception e) {
            return new StatusDto(StatusTask.NotFound.toString());
        }
    }

    @GetMapping("/count-queue")
    @Operation(
            summary = "Получить количество задач в очереди",
            description = "Возвращает текущее количество задач в очереди"
    )
    @ApiResponse(responseCode = "200", description = "Количество успешно получено")
    public int getCountQueue() {
        return taskService.getCount();
    }

    @GetMapping("/get-run-task")
    @Operation(
            summary = "Получить выполняющиеся задачи",
            description = "Возвращает список задач, находящихся в процессе выполнения"
    )
    @ApiResponse(responseCode = "200", description = "Список выполняющихся задач успешно получен")
    public List<TaskInQueueDto> getRunTask() {
        return taskService.getRunTask();
    }

    @PostMapping("/task/{id}/ban")
    @Operation(
            summary = "Забанить задачу",
            description = "Помечает задачу как запрещенную по ID"
    )
    @ApiResponse(responseCode = "200", description = "Задача успешно заблокирована")
    public void banTask(
            @Parameter(description = "ID задачи", example = "10")
            @PathVariable("id") long taskId
    ) {
        taskService.banTask(taskId);
    }
}
