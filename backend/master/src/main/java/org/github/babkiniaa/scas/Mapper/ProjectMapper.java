package org.github.babkiniaa.scas.Mapper;

import org.github.babkiniaa.scas.dto.project.CreateProjectDto;
import org.github.babkiniaa.scas.dto.project.ProjectDto;
import org.github.babkiniaa.scas.dto.project.ProjectDtoAll;
import org.github.babkiniaa.scas.entity.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    Project projectToEntity(CreateProjectDto projectDto);

    ProjectDto projectToDto(Project project);

    List<ProjectDto> projectToListDto(List<Project> projects);

    List<ProjectDtoAll> projectToListAll(List<Project> projects);


}
