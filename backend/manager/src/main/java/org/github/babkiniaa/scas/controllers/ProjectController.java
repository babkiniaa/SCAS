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

/**
 * Контроллер для управления проектами. Предоставляет API для создания проектов и получения списка проектов.
 */
@RequiredArgsConstructor
@RequestMapping("/project")
@CrossOrigin(origins = "http://localhost:9000")
@RestController
public class ProjectController {

    private final ProjectService projectService;
    private final AuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final ProjectMapper projectMapper;

    /**
     * Создает новый проект для текущего аутентифицированного пользователя.
     *
     * @param projectDto DTO с данными для создания проекта.
     * @return {@link ResponseEntity} с сообщением о статусе создания проекта.
     * @throws NotFoundUser если пользователь, выполняющий запрос, не найден.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto) throws NotFoundUser {
        User user = userService.findByUsername(authenticationFacade.getCurrentUserName())
                .orElseThrow(() -> new NotFoundUser("Пользователь не найден"));
        projectService.create(projectDto, user.getId());

        return ResponseEntity.ok("Created project");
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
