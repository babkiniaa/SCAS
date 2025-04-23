package org.github.babkiniaa.scas.service;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.AnalyserDto;
import org.github.babkiniaa.scas.mapper.RegisterTaskMapper;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@KafkaListener(topics = "task-create-events-topic", groupId = "task-create-events")
public class KafkaListeners {

    private final TaskService taskService;
    private final RegisterTaskMapper registerTaskMapper;

    @KafkaHandler
    @Transactional
    public void startAnalysis(AnalyserDto analyserDto) {
        taskService.saveTask(registerTaskMapper.toRegister(analyserDto));
    }
}
