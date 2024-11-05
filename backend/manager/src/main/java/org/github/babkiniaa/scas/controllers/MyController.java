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
    public int createReport(@RequestBody ProjectDto projectDto) {

        return agentServiceClient.createReport(projectDto);
    }

    @PostMapping("report/get-owasp/")
    public ReportOWASPDto getReport(@RequestBody int idOWASP) {

        return agentServiceClient.getReport(idOWASP);
    }

    @PostMapping("/project/create")
    public int createProject(@RequestBody ProjectAndUserIdDto projectAndUserIdDto) {

        return agentServiceClient.createProject(projectAndUserIdDto);
    }

    @PostMapping("/project/get-projects")
    public List<ProjectDto> getProject(@RequestBody GetProjectDto projectsDto) {

        return agentServiceClient.getProject(projectsDto);
    }

    @GetMapping("report/get-all-owasp")
    public List<ReportOWASPDto> getAllReportsOwasp() {

        return agentServiceClient.getAllReportsOwasp();
    }

    @GetMapping("report/get-all-pmd")
    public List<ReportPMDDto> getAllReportsPmd() {

        return agentServiceClient.getAllReportsPmd();
    }

    @GetMapping("report/get-all-checkstyle")
    public List<ReportCheckStyleDto> getAllReportsCheckstyle() {

        return agentServiceClient.getAllReportsCheckstyle();
    }

    @GetMapping("report/get-all-spotbugs")
    public List<ReportSpotBugsDto> getAllReportsSpotBugs() {

        return agentServiceClient.getAllReportsSpotBugs();
    }

    @PostMapping("report/get-owasp")
    public ReportOWASPDto getReportOwasp(@RequestBody Integer id){

        return agentServiceClient.getReportOwasp(id);
    }

    @PostMapping("report/get-pmd")
    public ReportPMDDto getReportPMD(@RequestBody Integer id){

        return agentServiceClient.getReportPMD(id);
    }

    @PostMapping("report/get-checkstyle")
    public ReportCheckStyleDto getReportCheckstyle(@RequestBody Integer id){

        return agentServiceClient.getReportCheckstyle(id);
    }

    @PostMapping("report/get-spotbugs")
    public ReportSpotBugsDto getReportSpotBugs(@RequestBody Integer id){

        return agentServiceClient.getReportSpotBugs(id);
    }


    @PostMapping("project/get-project")
    public ProjectDto getProject(@RequestBody Integer id){

        return agentServiceClient.getProject(id);
    }


    @PostMapping("project/connecting-report-pmd")
    public ResponseEntity<?> connectionUserAndReportPmd(@RequestBody ProjectIdAndReportId projectIdAndReportId){

        return agentServiceClient.connectionUserAndReportPmd(projectIdAndReportId);
    }

    @PostMapping("project/connecting-report-owasp")
    public ResponseEntity<?> connectionUserAndReport(@RequestBody ProjectIdAndReportId projectIdAndReportId) {

        return agentServiceClient.connectionUserAndReport(projectIdAndReportId);
    }

    @GetMapping("project/get-all")
    public List<ProjectDto> getAllProject() {

        return agentServiceClient.getAllProject();
    }


}
