package org.github.babkiniaa.scas.service;

import lombok.AllArgsConstructor;
import org.github.babkiniaa.scas.Mapper.ProjectMapper;
import org.github.babkiniaa.scas.dto.project.CreateProjectDto;
import org.github.babkiniaa.scas.dto.project.GetProjecAllDto;
import org.github.babkiniaa.scas.dto.project.ProjectDto;
import org.github.babkiniaa.scas.entity.*;
import org.github.babkiniaa.scas.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Сервис для управления проектами. Предоставляет методы для создания проектов,
 * получения всех проектов, а также выборки проектов по фильтрам.
 */
@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final MetricsService metricsService;

    /**
     * Создает новый проект для пользователя с указанным идентификатором.
     *
     * @param projectDto DTO с данными проекта для создания.
     */
    public long create(CreateProjectDto projectDto) {
        Project project = projectMapper.projectToEntity(projectDto);
        project.setUserId(projectDto.getUserId());
        metricsService.userAnalysis(projectDto.getUserId(), 1);
        return projectRepository.save(project).getId();
    }

    /**
     * Метод для поиска проекта по его id в базе данных по id
     * @param id ID проекта котрыый мы собираемся искать
     * @return
     */
    public ProjectDto findById(long id){
        return projectMapper.projectToDto(projectRepository.findById(id).get());
    }

    /**
     * Возвращает список проектов, отфильтрованных и отсортированных по параметрам из {@link GetProjecAllDto}.
     * <p>
     * Если флаг {@code isMyProject} равен true, возвращаются только проекты пользователя с заданным идентификатором.
     * Если флаг {@code isMyProject} равен false, возвращаются только публичные проекты данного пользователя.
     *
     * @param projectsDto DTO с параметрами для фильтрации и сортировки проектов.
     * @return список проектов, соответствующих фильтру.
     */
    public List<Project> getAllProject(GetProjecAllDto projectsDto) {
        Pageable pageable = PageRequest.of(
                projectsDto.getPage(),
                projectsDto.getCount(),
                Sort.by(Sort.Direction.fromString(projectsDto.getSortDirection()), projectsDto.getSortingField())
        );

        return projectsDto.isMyProject()
                ? projectRepository
                .findAllByNameContainingAndUserId(projectsDto.getName(), projectsDto.getUserId(), pageable).getContent()
                : projectRepository
                .findByNameContainingAndUserIdAndVisibility(projectsDto.getName(), projectsDto.getUserId(), true, pageable).getContent();
    }
}
