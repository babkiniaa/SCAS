package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.Mapper.ProjectMapper;
import org.github.babkiniaa.scas.dto.ListReportDto;
import org.github.babkiniaa.scas.dto.project.CreateProjectDto;
import org.github.babkiniaa.scas.dto.project.GetProjecAllDto;
import org.github.babkiniaa.scas.dto.project.ProjectDto;
import org.github.babkiniaa.scas.dto.project.ProjectDtoAll;
import org.github.babkiniaa.scas.service.ProjectService;
import org.github.babkiniaa.scas.service.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления проектами. Предоставляет API для создания проектов и получения списка проектов.
 */
@RequiredArgsConstructor
@RequestMapping("/project")
@CrossOrigin(origins = "http://localhost:9000")
@RestController
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;
    private final ReportService reportService;

    @PostMapping("/create")
    public long createProject(@RequestBody CreateProjectDto createProjectDto) {

        return projectService.create(createProjectDto);
    }

    /**
     * Возвращает список проектов в соответствии с заданными параметрами.
     *
     * @param projectsDto DTO с параметрами для фильтрации и сортировки проектов.
     * @return список проектов, соответствующих заданным параметрам.
     */
    @PostMapping("/get-projects")
    public List<ProjectDto> getProjects(@RequestBody GetProjecAllDto projectsDto) {

        return projectMapper.projectToListDto(projectService.getAllProject(projectsDto));
    }

    @GetMapping("/get-project/{id}")
    public ProjectDto getProject(@PathVariable int id) {

        return projectService.findById(id);
    }

    @GetMapping("/get-all")
    public List<ProjectDtoAll> getAllProject(@RequestParam int page, @RequestParam String name) {
        List<ProjectDtoAll> projectDtoAlls = projectMapper.projectToListAll(projectService.findByAll(page, name));

        if (projectDtoAlls.isEmpty())
            return null;
        for (ProjectDtoAll projectDtoAll: projectDtoAlls) {
            List<ListReportDto> reportId  = reportService.findAllByProjectId(projectDtoAll.getId());
            if (reportId.isEmpty())
                continue;
            projectDtoAll.setCountBugs(reportService.countBugsLastReport(reportId.get(reportId.size()-1).getId()));

        }

        return projectDtoAlls;
    }


}
