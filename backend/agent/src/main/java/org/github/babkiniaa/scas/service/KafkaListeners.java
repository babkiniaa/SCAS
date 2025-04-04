package org.github.babkiniaa.scas.service;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.Request.RegisterTaskDto;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@KafkaListener(topics = "task-create-events-topic", groupId = "task-create-events")
public class KafkaListeners {

    private final TaskService taskService;

    @KafkaHandler
    @Transactional
    public void startAnalysis(RegisterTaskDto registerTaskDto) {
        taskService.saveTask(registerTaskDto);
    }
}
