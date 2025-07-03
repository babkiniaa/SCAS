package org.github.babkiniaa.scas.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.AgentServiceClient;
import org.github.babkiniaa.scas.client.MasterServiceClient;
import org.github.babkiniaa.scas.dto.ListReportDto;
import org.github.babkiniaa.scas.dto.StatusDto;
import org.github.babkiniaa.scas.dto.project.*;
import org.github.babkiniaa.scas.dto.Response.ReportDto;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyController {

    private final MasterServiceClient masterServiceClient;
    private final AgentServiceClient agentServiceClient;

    @Operation(summary = "Получить HashMap методов агента")
    @GetMapping("analysis/get-hashmap")
    public HashMap<String, List<String>> getMethodMap() {
        return agentServiceClient.getMethodMap();
    }

    @Operation(summary = "Создать отчет")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Отчет успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    @PostMapping("/report/create")
    public void createReport(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания отчета",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AnalyserDto.class))
            )
            AnalyserDto analyserDto) {
        masterServiceClient.createReport(analyserDto);
    }

    @Operation(summary = "Найти отчеты по ID проекта")
    @GetMapping("report/get-by-project/{id}")
    public List<ListReportDto> findReportsByProjectId(
            @PathVariable("id")
            @Parameter(description = "ID проекта", required = true) long projectId) {
        return masterServiceClient.findReportsByProjectId(projectId);
    }

    @Operation(summary = "Получить статус задачи по ID")
    @GetMapping("report/status/{id}")
    public StatusDto getStatus(
            @PathVariable("id")
            @Parameter(description = "ID задачи", required = true) long idTask) {
        return masterServiceClient.getStatus(idTask);
    }

    @Operation(summary = "Получить сводный отчет по проекту")
    @GetMapping("report/get-reports/{id}")
    public ReportDto getRep(
            @PathVariable("id")
            @Parameter(description = "ID проекта", required = true) long projectId) {
        return masterServiceClient.getRep(projectId);
    }

    @Operation(summary = "Получить конкретный отчет по ID")
    @GetMapping("report/{id}/find")
    public ReportDto getReport(
            @PathVariable("id")
            @Parameter(description = "ID отчета", required = true) Long idReport) {
        return masterServiceClient.getReport(idReport);
    }

    @Operation(summary = "Создать новый проект")
    @PostMapping("/project/create")
    public long createProject(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания проекта",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProjectCreateDto.class))
            )
            ProjectCreateDto projectCreateDto) {
        return masterServiceClient.createProject(projectCreateDto);
    }

    @Operation(summary = "Получить проекты по фильтрам")
    @PostMapping("/project/get-projects")
    public List<ProjectDto> getProject(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Параметры фильтрации проектов",
                    required = true,
                    content = @Content(schema = @Schema(implementation = GetProjectAllDto.class))
            )
            GetProjectAllDto projectsDto) {
        return masterServiceClient.getProject(projectsDto);
    }

    @Operation(summary = "Получить проект по ID")
    @GetMapping("project/get-project/{id}")
    public ProjectDto getProject(
            @PathVariable("id")
            @Parameter(description = "ID проекта", required = true) int id) {
        return masterServiceClient.getProject(id);
    }

    @Operation(summary = "Получить все проекты с пагинацией и фильтрацией по имени")
    @GetMapping("project/get-all")
    public List<ProjectDtoAll> getAllProject(
            @RequestParam
            @Parameter(description = "Номер страницы (начинается с 0)", required = true) int page,
            @RequestParam
            @Parameter(description = "Имя для фильтрации проектов", required = false) String name) {
        return masterServiceClient.getAllProject(page, name);
    }
}
