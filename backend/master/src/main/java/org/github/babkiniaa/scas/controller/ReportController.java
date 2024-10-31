package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.Mapper.*;
import org.github.babkiniaa.scas.client.AgentServiceClient;
import org.github.babkiniaa.scas.dto.*;
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
    public int createReport(@RequestBody ProjectDto projectDto) {
        ProjectDto projectDtoNew = agentServiceClient.init(projectDto);
        ReportPMDDto reportPmd = projectDtoNew.getReportPMDS().get(projectDtoNew.getReportPMDS().size() - 1);
        int idReportPMD = reportPMDService.create(reportPmd);
        return idReportPMD;
    }


    @PostMapping("/get-reports")
    public List<ProjectDto> getReports(@RequestBody GetProjectDto projectsDto) {
        return projectMapper.projectToListDto(projectService.getAllProject(projectsDto));
    }
}