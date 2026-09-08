package org.github.babkiniaa.scas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.Mapper.ProjectMapper;
import org.github.babkiniaa.scas.dto.ListReportDto;
import org.github.babkiniaa.scas.dto.project.CreateProjectDto;
import org.github.babkiniaa.scas.dto.project.GetProjecAllDto;
import org.github.babkiniaa.scas.dto.project.ProjectDto;
import org.github.babkiniaa.scas.dto.project.ProjectDtoAll;
import org.github.babkiniaa.scas.service.ProjectService;
import org.github.babkiniaa.scas.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
@CrossOrigin(origins = "http://localhost:9000")
@Tag(name = "Project Controller", description = "API для управления проектами")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;
    private final ReportService reportService;

    @PostMapping("/create")
    @Operation(summary = "Создать проект", description = "Создает новый проект на основе переданных данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Проект успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные проекта")
    })
    public long createProject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные для создания проекта")
            @RequestBody CreateProjectDto createProjectDto) {
        return projectService.create(createProjectDto);
    }

    @PostMapping("/get-projects")
    @Operation(summary = "Получить проекты", description = "Возвращает список проектов с фильтрацией и сортировкой")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Проекты успешно получены")
    })
    public List<ProjectDto> getProjects(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Фильтры и параметры запроса")
            @RequestBody GetProjecAllDto projectsDto) {
        return projectMapper.projectToListDto(projectService.getAllProject(projectsDto));
    }

    @GetMapping("/get-project/{id}")
    @Operation(summary = "Получить проект по ID", description = "Возвращает один проект по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Проект найден"),
            @ApiResponse(responseCode = "404", description = "Проект не найден")
    })
    public ProjectDto getProject(
            @Parameter(description = "ID проекта", example = "1")
            @PathVariable int id) {
        return projectService.findById(id);
    }

    @GetMapping("/get-all")
    @Operation(summary = "Получить все проекты с отчётами", description = "Возвращает список всех проектов с количеством ошибок в последнем отчёте")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список проектов успешно получен")
    })
    public List<ProjectDtoAll> getAllProject(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam int page,
            @Parameter(description = "Фильтр по имени проекта", example = "MyProject")
            @RequestParam String name) {

        List<ProjectDtoAll> projectDtoAlls = projectMapper.projectToListAll(projectService.findByAll(page, name));

        if (projectDtoAlls.isEmpty())
            return null;

        for (ProjectDtoAll projectDtoAll : projectDtoAlls) {
            List<ListReportDto> reportId = reportService.findAllByProjectId(projectDtoAll.getId());
            if (reportId.isEmpty()) continue;

            projectDtoAll.setCountBugs(
                    reportService.countBugsLastReport(reportId.get(reportId.size() - 1).getId())
            );
        }

        return projectDtoAlls;
    }

}
