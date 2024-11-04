package org.github.babkiniaa.scas.controllers;

import edu.umd.cs.findbugs.Project;
import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.MasterServiceClient;
import org.github.babkiniaa.scas.dto.*;
import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectAndUserIdDto;
import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectIdAndReportId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyController {

    private final MasterServiceClient agentServiceClient;

    @PostMapping("/report/create")
    public ResponseEntity<?> createReport(@RequestBody ProjectDto projectDto) {

        return agentServiceClient.createReport(projectDto);
    }

    @PostMapping("report/get-owasp/")
    ReportOWASPDto getReport(@RequestBody int idOWASP) {

        return agentServiceClient.getReport(idOWASP);
    }

    @PostMapping("/project/create")
    int createProject(@RequestBody ProjectAndUserIdDto projectAndUserIdDto) {

        return agentServiceClient.createProject(projectAndUserIdDto);
    }

    @PostMapping("/project/get-reports")
    List<ProjectDto> getProject(@RequestBody GetProjectDto projectsDto) {

        return agentServiceClient.getProject(projectsDto);
    }

    @GetMapping("report/get-all-owasp")
    List<ReportOWASPDto> getAllReportsOwasp() {

        return agentServiceClient.getAllReportsOwasp();
    }

    @GetMapping("report/get-all-pmd")
    List<ReportPMDDto> getAllReportsPmd() {

        return agentServiceClient.getAllReportsPmd();
    }

    @GetMapping("report/get-all-checkstyle")
    List<ReportCheckStyleDto> getAllReportsCheckstyle() {

        return agentServiceClient.getAllReportsCheckstyle();
    }

    @GetMapping("report/get-all-spotbugs")
    List<ReportSpotBugsDto> getAllReportsSpotBugs() {

        return agentServiceClient.getAllReportsSpotBugs();
    }

    @PostMapping("project/connecting-report")
    ResponseEntity<?> connectionUserAndReport(@RequestBody ProjectIdAndReportId projectIdAndReportId) {

        return agentServiceClient.connectionUserAndReport(projectIdAndReportId);
    }

    @PostMapping("project/get-all")
    List<ProjectDto> getAllProject() {

        return agentServiceClient.getAllProject();
    }

}
