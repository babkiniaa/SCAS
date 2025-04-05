package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.AgentServiceClient;
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

    private final KafkaTemplate<String, AnalyserDto> kafkaTemplate;
    private final ProjectService projectService;
    private final ReportService reportService;
    private final AgentServiceClient agentServiceClient;
    private final MetricsService metricsService;


    @PostMapping("/create")
    public void createReport(@RequestBody AnalyserDto analyserDto) {
        analyserDto.setUrl(projectService.findById(analyserDto.getIdProject()).getUrl());
        metricsService.userTask(projectService.findByIdProject(analyserDto.getIdProject()).getUserId(), 1);
        String prodactId = UUID.randomUUID().toString();
        CompletableFuture<SendResult<String, AnalyserDto>> future =
                kafkaTemplate.send("task-create-events-topic", prodactId, analyserDto);

//        long taskId = agentServiceClient.init(analyserDto);

//        return taskId;
    }

    @GetMapping("/status/{id}")
    public String getStatus(@PathVariable("id") long projectId) {
        return agentServiceClient.getStatus(projectId);
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