package org.github.babkiniaa.scas.mapper;

import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    Project projectToEntity(ProjectDto projectDto);

    ProjectDto projectToDto(Project project);

    List<ProjectDto> projectToListDto(List<Project> projects);
}
