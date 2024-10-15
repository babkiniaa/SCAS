package org.github.babkiniaa.scas.Mapper;

import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.entity.Project;
import org.github.babkiniaa.scas.entity.Report;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    Project projectToEntity(ProjectDto projectDto);

    ProjectDto projectToDto(Project project);

    List<ProjectDto> projectToListDto(List<Project> projects);
}
