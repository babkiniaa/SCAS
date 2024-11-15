package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.Mapper.*;
import org.github.babkiniaa.scas.client.AgentServiceClient;
import org.github.babkiniaa.scas.dto.*;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/report")
@CrossOrigin(origins = "http://localhost:9000")
@RestController
public class ReportController {

    private final ProjectService projectService;
    private final ReportService reportService;
    private final ProjectMapper projectMapper;
    private final AgentServiceClient agentServiceClient;
    private final ReportMapper reportMapper;

    @PostMapping("/create")
    public long createReport(@RequestBody AnalyserDto analyserDto) {
        StartAnalyseDto startAnalyseDto = projectService.startInfo(analyserDto);
        long taskId = agentServiceClient.init(startAnalyseDto);

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
    public List<ReportDto> findReportsByProjectId(@PathVariable("id") long projectId){
        return reportService.findAllByProjectId(projectId);
    }

    @PostMapping("/save/{id}")
    public void saveReport(@PathVariable("id") long idProject,@RequestBody ReportDto reportDto){
        reportService.save(idProject, reportDto);
    }

}