package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.Mapper.ProjectMapper;
import org.github.babkiniaa.scas.dto.project.GetProjecAllDto;
import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectAndUserIdDto;
import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectIdAndReportId;
import org.github.babkiniaa.scas.dto.project.ProjectDto;
import org.github.babkiniaa.scas.service.ProjectService;
import org.github.babkiniaa.scas.Mapper.ProjectUserId.ProjectAndUserIdMapper;
import org.springframework.http.ResponseEntity;
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
    private final ProjectAndUserIdMapper projectAndUserMapper;

    @PostMapping("/create")
    public long createProject(@RequestBody ProjectAndUserIdDto projectAndUserIdDto) {
        ProjectDto projectDto = projectAndUserMapper.projectDtoToProjectAndUserIdDto(projectAndUserIdDto);
        return projectService.create(projectDto, projectAndUserIdDto.getUserId());
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

}
