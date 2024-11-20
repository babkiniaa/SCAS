package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.AgentServiceClient;
import org.github.babkiniaa.scas.dto.*;
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

    @PostMapping("/create")
    public long createReport(@RequestBody AnalyserDto analyserDto) {
        analyserDto.setUrl(projectService.findById(analyserDto.getIdProject()).getUrl());
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
    public List<ListReportDto> findReportsByProjectId(@PathVariable("id") long projectId){
        return reportService.findAllByProjectId(projectId);
    }

    @PostMapping("/save/{id}")
    public void saveReport(@PathVariable("id") long idProject,@RequestBody ReportDto reportDto){
        reportService.save(idProject, reportDto);
    }

    @GetMapping("/get-analyzers")
    public List<String> getAnalyzers(@RequestParam String hash){
        return reportService.findAnalyzes(hash);
    }

}