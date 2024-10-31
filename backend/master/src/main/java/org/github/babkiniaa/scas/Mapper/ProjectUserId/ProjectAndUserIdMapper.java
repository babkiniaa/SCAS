package org.github.babkiniaa.scas.Mapper.ProjectUserId;

import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectAndUserIdDto;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectAndUserIdMapper {
    ProjectDto projectDtoToProjectAndUserIdDto(ProjectAndUserIdDto projectAndUserIdDto);
}
