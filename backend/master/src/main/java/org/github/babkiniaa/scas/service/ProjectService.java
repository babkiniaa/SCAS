package org.github.babkiniaa.scas.service;

import lombok.AllArgsConstructor;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.entity.Project;
import org.github.babkiniaa.scas.Mapper.ProjectMapper;
import org.github.babkiniaa.scas.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Project create(ProjectDto projectDto) {
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

}
