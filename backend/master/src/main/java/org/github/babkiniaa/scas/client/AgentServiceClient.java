package org.github.babkiniaa.scas.client;

import org.github.babkiniaa.scas.dto.AnalyserDto;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.dto.TaskInQueueDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "agent-service", url = "http://localhost:8081")
public interface AgentServiceClient {

    @PostMapping("/analysis/start/queue")
    long init(@RequestBody AnalyserDto startAnalyseDto);

    @GetMapping("/analysis/task/{id}/status")
    String getStatus(@PathVariable("id") long id);

    @GetMapping("/analysis/task/{id}/report")
    ReportDto getReport(@PathVariable("id") long id);

    @GetMapping("/analysis/count-queue")
    int getCountQueue();

    @GetMapping("/analysis/get-run-task")
    List<TaskInQueueDto> getRunTask();

    @PostMapping("/analysis/task/ban/{id}")
    void banTask(@PathVariable("id") long taskId);
}
