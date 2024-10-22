package org.github.babkiniaa.scas.controllers;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.Mapper.ProjectMapper;
import org.github.babkiniaa.scas.dto.GetProjectDto;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.entity.Project;
import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.exception.NotFoundUser;
import org.github.babkiniaa.scas.security.AuthenticationFacade;
import org.github.babkiniaa.scas.service.ProjectService;
import org.github.babkiniaa.scas.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/project")
@CrossOrigin(origins = "http://localhost:9000")
public class ProjectController {

    private final ProjectService projectService;
    private final AuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final ProjectMapper projectMapper;

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto) throws NotFoundUser {
        User user = userService.findByUsername(authenticationFacade.getCurrentUserName())
                .orElseThrow(() -> new NotFoundUser("Пользователь не найден"));
        projectService.create(projectDto, user.getId());

        return ResponseEntity.ok("Created project");
    }

    @PostMapping("/get-projects")
    public List<ProjectDto> getProject(@RequestBody GetProjectDto projectsDto) {

        return projectMapper.projectToListDto(projectService.getAllProject(projectsDto));
    }

}
