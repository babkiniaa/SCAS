package org.github.babkiniaa.scas.controllers;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.AgentServiceClient;
import org.github.babkiniaa.scas.client.MasterServiceClient;
import org.github.babkiniaa.scas.dto.ListReportDto;
import org.github.babkiniaa.scas.dto.project.*;
import org.github.babkiniaa.scas.dto.Response.ReportDto;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyController {

    private final MasterServiceClient masterServiceClient;
    private final AgentServiceClient agentServiceClient;

    @GetMapping("analysis/get-hashmap")
    public HashMap<String, List<String>> getMethodMap() {

        return agentServiceClient.getMethodMap();
    }

    @PostMapping("/report/create")
    public void createReport(@RequestBody AnalyserDto analyserDto) {
        masterServiceClient.createReport(analyserDto);
    }

    @GetMapping("report/get-by-project/{id}")
    public List<ListReportDto> findReportsByProjectId(@PathVariable("id") long projectId) {
        return masterServiceClient.findReportsByProjectId(projectId);
    }

    @GetMapping("report/status/{id}")
    public String getStatus(@PathVariable("id") long idTask) {
        return masterServiceClient.getStatus(idTask);
    }

    @GetMapping("report/get-reports/{id}")
    ReportDto getRep(@PathVariable("id") long projectId) {
        return masterServiceClient.getRep(projectId);
    }

    @GetMapping("report/find/{id}")
    public ReportDto getReport(@PathVariable("id") Long idReport) {
        return masterServiceClient.getReport(idReport);
    }


    @PostMapping("/project/create")
    public long createProject(@RequestBody ProjectCreateDto projectCreateDto) {

        return masterServiceClient.createProject(projectCreateDto);
    }

    @PostMapping("/project/get-projects")
    public List<ProjectDto> getProject(@RequestBody GetProjectAllDto projectsDto) {

        return masterServiceClient.getProject(projectsDto);
    }

    @GetMapping("project/get-project/{id}")
    public ProjectDto getProject(@PathVariable("id") int id) {

        return masterServiceClient.getProject(id);
    }

    @GetMapping("project/get-all")
    public List<ProjectDtoAll> getAllProject(@RequestParam int page, @RequestParam String name) {

        return masterServiceClient.getAllProject(page, name);
    }
}
