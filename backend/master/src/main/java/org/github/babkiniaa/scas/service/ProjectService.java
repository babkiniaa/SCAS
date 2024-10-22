package org.github.babkiniaa.scas.service;

import lombok.AllArgsConstructor;
import org.github.babkiniaa.scas.dto.GetProjectDto;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.entity.Project;
import org.github.babkiniaa.scas.Mapper.ProjectMapper;
import org.github.babkiniaa.scas.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectService {
    @Autowired
    private final ProjectRepository projectRepository;
    @Autowired
    private final ProjectMapper projectMapper;

    public Project create(ProjectDto projectDto, long id) {
        Project project = projectMapper.projectToEntity(projectDto);
        project.setUserId(id);
        return projectRepository.save(projectMapper.projectToEntity(projectDto));
    }


    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project update(ProjectDto projectDto) {
        return projectRepository.save(projectMapper.projectToEntity(projectDto));
    }

    public Optional<Project> findById(Integer id) {
        return projectRepository.findById(id);
    }

    public List<Project> getAllProject(GetProjectDto projectsDto) {
        Pageable pageable = PageRequest.of(
                projectsDto.getPage(),
                projectsDto.getCount(),
                Sort.by(projectsDto.getSortingField())
        );

        return projectsDto.isMyProject()
                ? projectRepository
                    .findByUserId(projectsDto.getUserId(), pageable).getContent()
                : projectRepository
                    .findByUserIdAndVisibility(projectsDto.getUserId(), true, pageable).getContent();
    }
}
