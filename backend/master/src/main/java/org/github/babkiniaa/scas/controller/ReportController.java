package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.AgentServiceClient;
import org.github.babkiniaa.scas.configuration.TaskQueue;
import org.github.babkiniaa.scas.dto.*;
import org.github.babkiniaa.scas.dto.Response.ReportAndIdProjectDto;
import org.github.babkiniaa.scas.service.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RequestMapping("/report")
@CrossOrigin(origins = "http://localhost:9000")
@RestController
public class ReportController {

    private final ProjectService projectService;
    private final ReportService reportService;
    private final AgentServiceClient agentServiceClient;
    private final MetricsService metricsService;
    private final TaskQueue taskQueue;


    @PostMapping("/create")
    public void createReport(@RequestBody AnalyserDto analyserDto) throws InterruptedException {
        analyserDto.setUrl(projectService.findById(analyserDto.getIdProject()).getUrl());
        metricsService.userTask(projectService.findByIdProject(analyserDto.getIdProject()).getUserId(), 1);
        taskQueue.addTask(analyserDto);
    }

    @GetMapping("/status/{id}")
    public StatusDto getStatus(@PathVariable("id") long projectId) {
        StatusDto statusTask = agentServiceClient.getStatus(projectId);

        if (statusTask.getStat().equals("NotFound")){
            return new StatusDto(taskQueue.getTaskStatus(projectId));
        }

        return statusTask;
    }

    @GetMapping("/find/{id}")
    public ReportDto findReport(@PathVariable("id") long reportId) {
        return reportService.findById(reportId);
    }

    @GetMapping("/get-by-project/{id}")
    public List<ListReportDto> findReportsByProjectId(@PathVariable("id") long projectId){
        return reportService.findAllByProjectId(projectId);
    }

    @GetMapping("/get-analyzers")
    public List<String> getAnalyzers(@RequestParam String hash, @RequestParam long projectId){
        return reportService.findAnalyzes(hash, projectId);
    }

}