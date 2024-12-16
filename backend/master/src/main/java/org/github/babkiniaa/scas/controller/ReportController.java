package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.AgentServiceClient;
import org.github.babkiniaa.scas.dto.*;
import org.github.babkiniaa.scas.entity.ProjectIdAndReportId;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/report")
@CrossOrigin(origins = "http://localhost:9000")
@RestController
public class ReportController {

    private final ProjectService projectService;
    private final ReportService reportService;
    private final AgentServiceClient agentServiceClient;
    private final MetricsService metricsService;


    @PostMapping("/create/offline")
    public long createReportOffline(@RequestBody ProjectIdAndReportId projectIdAndReportId) {
        long taskId = agentServiceClient.init(reportService.startOffline(projectIdAndReportId));

        return taskId;
    }

    @PostMapping("/create")
    public long createReport(@RequestBody AnalyserDto analyserDto) {
        analyserDto.setUrl(projectService.findById(analyserDto.getIdProject()).getUrl());
        metricsService.userTask(projectService.findByIdProject(analyserDto.getIdProject()).getUserId(), 1);
        long taskId = agentServiceClient.init(analyserDto);

        return taskId;
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
    public List<ListReportDto> findReportsByProjectId(@PathVariable("id") long projectId) {
        return reportService.findAllByProjectId(projectId);
    }

    @GetMapping("/get-analyzers")
    public List<String> getAnalyzers(@RequestParam String hash, @RequestParam long projectId) {
        return reportService.findAnalyzes(hash, projectId);
    }

}