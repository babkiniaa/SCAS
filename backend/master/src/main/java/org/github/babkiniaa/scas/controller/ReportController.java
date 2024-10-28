package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.Mapper.*;
import org.github.babkiniaa.scas.client.AgentServiceClient;
import org.github.babkiniaa.scas.dto.GetProjectDto;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.dto.ReportPMDDto;
import org.github.babkiniaa.scas.entity.ReportOWASP;
import org.github.babkiniaa.scas.entity.ReportSpotBugs;
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
    private final ReportSpotBugsService reportSpotBugsService;
    private final ReportOWASPService reportOWASPService;
    private final ReportPMDService reportPMDService;
    private final ReportCheckStyleService reportCheckStyleService;
    private final ReportSpotBugsMapper reportSpotBugsMapper;
    private final ReportOWASPMapper reportOWASPMapper;
    private final ReportPMDMapper reportPMDMapper;
    private final ReportCheckStyleMapper reportCheckStyleMapper;
    private final ProjectMapper projectMapper;
    private final AgentServiceClient agentServiceClient;

    @PostMapping("/create")
    public ResponseEntity<?> createReport(@RequestBody ProjectDto projectDto) {
        projectDto = agentServiceClient.init(projectDto.getUrl());
        return ResponseEntity.ok("Created project");
    }


    @PostMapping("/get-reports")
    public List<ProjectDto> getProject(@RequestBody GetProjectDto projectsDto) {
        return projectMapper.projectToListDto(projectService.getAllProject(projectsDto));
    }
}