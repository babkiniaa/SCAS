package org.github.babkiniaa.scas.mappers;

import org.github.babkiniaa.scas.dto.project.ProjectDto;
import org.github.babkiniaa.scas.dto.StartAnalysisDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StartAnalysisMapper {

    StartAnalysisDto toStartAnalysisDto(ProjectDto projectDto);
}
