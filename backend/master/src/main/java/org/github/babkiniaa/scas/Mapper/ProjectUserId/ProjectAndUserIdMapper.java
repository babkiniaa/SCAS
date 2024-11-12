package org.github.babkiniaa.scas.Mapper.ProjectUserId;

import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectAndUserIdDto;
import org.github.babkiniaa.scas.dto.project.ProjectDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectAndUserIdMapper {
    ProjectDto projectDtoToProjectAndUserIdDto(ProjectAndUserIdDto projectAndUserIdDto);
}
