package org.github.babkiniaa.scas.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.Request.RegisterTaskDto;
import org.github.babkiniaa.scas.entity.Task;
import org.github.babkiniaa.scas.mapper.TaskMapper;
import org.github.babkiniaa.scas.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public Long saveTask(RegisterTaskDto registerTaskDto) {
        return taskRepository.save(taskMapper.RegisterTaskToTask(registerTaskDto)).getId();
    }


}
