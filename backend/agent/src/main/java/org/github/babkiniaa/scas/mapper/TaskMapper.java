package org.github.babkiniaa.scas.mapper;

import org.github.babkiniaa.scas.dto.Request.RegisterTaskDto;
import org.github.babkiniaa.scas.dto.Request.StartAnalyseDto;
import org.github.babkiniaa.scas.dto.Response.TaskInQueueDto;
import org.github.babkiniaa.scas.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task registerTaskToTask(RegisterTaskDto registerTaskDto);

    TaskInQueueDto taskToTaskQueue (Task task);

    StartAnalyseDto registerTaskToStartAnalyze(RegisterTaskDto registerTaskDto);
}
