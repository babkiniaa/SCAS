package org.github.babkiniaa.scas.mapper;

import org.github.babkiniaa.scas.dto.Request.RegisterTaskDto;
import org.github.babkiniaa.scas.entity.Task;
import org.mapstruct.Mapper;

@Mapper
public interface TaskMapper {
    Task RegisterTaskToTask (RegisterTaskDto registerTaskDto);
}
