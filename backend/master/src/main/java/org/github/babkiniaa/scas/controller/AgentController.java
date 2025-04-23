package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.AgentServiceClient;
import org.github.babkiniaa.scas.configuration.TaskQueue;
import org.github.babkiniaa.scas.dto.TaskInQueueDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agent")
public class AgentController {

    private final AgentServiceClient agentServiceClient;
    private final TaskQueue taskQueue;

    @GetMapping("/count-queue")
    public int getCountQueue(){
        return taskQueue.count();
    }

    @GetMapping("/get-run-task")
    public List<TaskInQueueDto> getRunTask(){
        return agentServiceClient.getRunTask();
    }

    @PostMapping("/task/ban/{id}")
    public void banTask(@PathVariable("id") long taskId){
        taskQueue.banTask(taskId);
    }

}
