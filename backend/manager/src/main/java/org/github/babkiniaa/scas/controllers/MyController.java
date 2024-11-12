package org.github.babkiniaa.scas.controllers;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.AgentServiceClient;
import org.github.babkiniaa.scas.client.MasterServiceClient;
import org.github.babkiniaa.scas.dto.*;
import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectAndUserIdDto;
import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectIdAndReportId;
import org.github.babkiniaa.scas.dto.Response.ReportDto;
import org.github.babkiniaa.scas.dto.project.AnalyserDto;
import org.github.babkiniaa.scas.dto.project.GetProjectAllDto;
import org.github.babkiniaa.scas.dto.project.ProjectDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyController {

    private final MasterServiceClient masterServiceClient;
    private final AgentServiceClient agentServiceClient;

    @GetMapping("analysis/get-hashmap")
    public HashMap<String, List<String>>  getMethodMap() {

        return agentServiceClient.getMethodMap();
    }

    @PostMapping("/report/create")
    public long createReport(@RequestBody AnalyserDto analyserDto) {
        return masterServiceClient.createReport(analyserDto);
    }

    @GetMapping("report/status/{id}")
    public String getStatus(@PathVariable("id") long idTask){
        return masterServiceClient.getStatus(idTask);
    }

    @GetMapping("report/find/{id}")
    public ReportDto getReport(@PathVariable("id") Long idReport){
        return masterServiceClient.getReport(idReport);
    }

    @GetMapping("report/save/{id}")
    public ReportDto reportSave(@PathVariable("id") long idProject){
        return masterServiceClient.getReport(idProject);
    }

    @PostMapping("/project/create")
    public long createProject(@RequestBody ProjectAndUserIdDto projectAndUserIdDto) {

        return masterServiceClient.createProject(projectAndUserIdDto);
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
    public List<ProjectDto> getAllProject() {

        return masterServiceClient.getAllProject();
    }


}
