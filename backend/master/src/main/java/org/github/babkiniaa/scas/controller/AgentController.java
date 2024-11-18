package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.AgentServiceClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agent")
public class AgentController {

    private final AgentServiceClient agentServiceClient;

    @GetMapping("/count-queue")
    public int getCountQueue(){
        return agentServiceClient.getCountQueue();
    }

    @GetMapping("/get-run-task")
    public List<Long> getRunTask(){
        return agentServiceClient.getRunTask();
    }

    @PostMapping("/task/ban/{id}")
    public void banTask(@PathVariable("id") long taskId){
        agentServiceClient.banTask(taskId);
    }

}
