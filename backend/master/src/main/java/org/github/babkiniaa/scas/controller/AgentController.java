package org.github.babkiniaa.scas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.AgentServiceClient;
import org.github.babkiniaa.scas.configuration.TaskQueue;
import org.github.babkiniaa.scas.dto.TaskInQueueDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agent")
@Tag(name = "Agent Controller", description = "Операции взаимодействия с агентом и очередью задач")
public class AgentController {

    private final AgentServiceClient agentServiceClient;
    private final TaskQueue taskQueue;

    @GetMapping("/count-queue")
    @Operation(summary = "Получить количество задач в очереди", description = "Возвращает текущее количество задач, находящихся в очереди агента")
    @ApiResponse(responseCode = "200", description = "Количество задач успешно получено")
    public int getCountQueue() {
        return taskQueue.count();
    }

    @GetMapping("/get-run-task")
    @Operation(summary = "Получить список запущенных задач", description = "Возвращает список задач, которые в настоящее время выполняются агентом")
    @ApiResponse(responseCode = "200", description = "Список запущенных задач успешно получен")
    public List<TaskInQueueDto> getRunTask() {
        return agentServiceClient.getRunTask();
    }

    @PostMapping("/task/{id}/ban")
    @Operation(
            summary = "Заблокировать задачу",
            description = "Добавляет задачу в бан-лист по её идентификатору, исключая её из выполнения"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно заблокирована"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public void banTask(
            @Parameter(description = "ID задачи, которую необходимо заблокировать", example = "123")
            @PathVariable("id") long taskId
    ) {
        taskQueue.banTask(taskId);
    }

}
