package org.github.babkiniaa.scas.Controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.Request.RegisterTaskDto;
import org.github.babkiniaa.scas.dto.Response.ReportDto;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.entity.StatusTask;
import org.github.babkiniaa.scas.mapper.*;
import org.github.babkiniaa.scas.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class ChallengeController {

    private final TaskService taskService;

    @PostMapping("/start/queue")
    public Long initTask(@RequestBody RegisterTaskDto registerTaskDto) throws Exception {
        return taskService.saveTask(registerTaskDto);
    }

    @GetMapping("/task/{id}/status")
    public StatusTask initTask(@PathVariable long id) throws Exception {
        return taskService.getStatus(id);
    }

    @GetMapping("/get-hashmap")
    public HashMap<String, List<String>> getMethodMap() {
        return taskService.getMethodMap();
    }

    @GetMapping("/task/{id}/report")
    public ReportDto getReport(@PathVariable long id) {
        return taskService.getReport(id);
    }

}
