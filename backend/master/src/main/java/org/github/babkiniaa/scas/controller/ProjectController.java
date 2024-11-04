package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.Mapper.ProjectMapper;
import org.github.babkiniaa.scas.dto.GetProjectDto;
import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectAndUserIdDto;
import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectIdAndReportId;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.entity.Project;
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
    public int createProject(@RequestBody ProjectAndUserIdDto projectAndUserIdDto) {
        ProjectDto projectDto = projectAndUserMapper.projectDtoToProjectAndUserIdDto(projectAndUserIdDto);
        return projectService.create(projectDto, projectAndUserIdDto.getUserId());
    }

    @PostMapping("/connecting-report")
    public ResponseEntity<?> connectionUserAndReport(@RequestBody ProjectIdAndReportId projectIdAndReportId){
        projectService.connectingReportOWASPAndProject(projectIdAndReportId.getProjectId(), projectIdAndReportId.getReportId());
        return ResponseEntity.ok("");
    }

    @PostMapping("/get-all")
    public List<ProjectDto> getAllProject(){
        return projectService.findAll();
    }

    /**
     * Возвращает список проектов в соответствии с заданными параметрами.
     *
     * @param projectsDto DTO с параметрами для фильтрации и сортировки проектов.
     * @return список проектов, соответствующих заданным параметрам.
     */
    @PostMapping("/get-projects")
    public List<ProjectDto> getProject(@RequestBody GetProjectDto projectsDto) {

        return projectMapper.projectToListDto(projectService.getAllProject(projectsDto));
    }
}
