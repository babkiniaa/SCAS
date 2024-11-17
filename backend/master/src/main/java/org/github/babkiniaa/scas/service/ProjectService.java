package org.github.babkiniaa.scas.service;

import lombok.AllArgsConstructor;
import org.github.babkiniaa.scas.Mapper.ProjectMapper;
import org.github.babkiniaa.scas.dto.AnalyserDto;
import org.github.babkiniaa.scas.dto.StartAnalyseDto;
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

    /**
     * Создает новый проект для пользователя с указанным идентификатором.
     *
     * @param projectDto DTO с данными проекта для создания.
     * @param id         идентификатор пользователя, которому будет принадлежать проект.
     */
    public long create(ProjectDto projectDto, long id) {
        Project project = projectMapper.projectToEntity(projectDto);
        project.setUserId(id);
        return projectRepository.save(project).getId();
    }

    public List<ProjectDto> findAll() {
        return projectMapper.projectToListDto(projectRepository.findAll());
    }

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


    public StartAnalyseDto startInfo(AnalyserDto analyserDto) {
        StartAnalyseDto startAnalyseDto = new StartAnalyseDto();
        startAnalyseDto.setUrl(findById(analyserDto.getIdProject()).getUrl());
        startAnalyseDto.setProjectId(analyserDto.getIdProject());
        startAnalyseDto.setNeedReports(analyserDto.getNeedReports());
        return startAnalyseDto;
    }

}
