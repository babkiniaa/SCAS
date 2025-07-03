package org.github.babkiniaa.scas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.AgentServiceClient;
import org.github.babkiniaa.scas.configuration.TaskQueue;
import org.github.babkiniaa.scas.dto.*;
import org.github.babkiniaa.scas.dto.Response.ReportAndIdProjectDto;
import org.github.babkiniaa.scas.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
@CrossOrigin(origins = "http://localhost:9000")
@Tag(name = "Report Controller", description = "API для управления отчетами и их статусами")
public class ReportController {

    private final ProjectService projectService;
    private final ReportService reportService;
    private final AgentServiceClient agentServiceClient;
    private final MetricsService metricsService;
    private final TaskQueue taskQueue;

    @PostMapping("/create")
    @Operation(
            summary = "Создать отчет",
            description = "Создает отчет на основе проекта и добавляет задачу в очередь"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно добавлена"),
            @ApiResponse(responseCode = "400", description = "Ошибка при создании задачи")
    })
    public void createReport(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для анализа проекта")
            @RequestBody AnalyserDto analyserDto
    ) throws InterruptedException {
        analyserDto.setUrl(projectService.findById(analyserDto.getIdProject()).getUrl());
        metricsService.userTask(projectService.findByIdProject(analyserDto.getIdProject()).getUserId(), 1);
        taskQueue.addTask(analyserDto);
    }

    @GetMapping("/{id}/status")
    @Operation(
            summary = "Получить статус задачи",
            description = "Возвращает статус задачи по ID проекта"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус успешно получен"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public StatusDto getStatus(
            @Parameter(description = "ID проекта", example = "1")
            @PathVariable("id") long projectId
    ) {
        StatusDto statusTask = agentServiceClient.getStatus(projectId);
        if (statusTask.getStat().equals("NotFound")) {
            return new StatusDto(taskQueue.getTaskStatus(projectId));
        }
        return statusTask;
    }

    @GetMapping("/{id}/find")
    @Operation(
            summary = "Найти отчет по ID",
            description = "Возвращает отчет по его уникальному идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчет найден"),
            @ApiResponse(responseCode = "404", description = "Отчет не найден")
    })
    public ReportDto findReport(
            @Parameter(description = "ID отчета", example = "100")
            @PathVariable("id") long reportId
    ) {
        return reportService.findById(reportId);
    }

    @GetMapping("/get-by-project/{id}")
    @Operation(
            summary = "Получить все отчеты проекта",
            description = "Возвращает список всех отчетов, связанных с указанным проектом"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчеты успешно получены")
    })
    public List<ListReportDto> findReportsByProjectId(
            @Parameter(description = "ID проекта", example = "5")
            @PathVariable("id") long projectId
    ) {
        return reportService.findAllByProjectId(projectId);
    }

    @GetMapping("/get-analyzers")
    @Operation(
            summary = "Получить использованные анализаторы",
            description = "Возвращает список названий анализаторов по хэшу и ID проекта"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список анализаторов успешно получен")
    })
    public List<String> getAnalyzers(
            @Parameter(description = "Хэш отчета", example = "abc123")
            @RequestParam String hash,
            @Parameter(description = "ID проекта", example = "5")
            @RequestParam long projectId
    ) {
        return reportService.findAnalyzes(hash, projectId);
    }

}
