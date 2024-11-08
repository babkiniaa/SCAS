package org.github.babkiniaa.scas.controllers;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.client.AgentServiceClient;
import org.github.babkiniaa.scas.client.MasterServiceClient;
import org.github.babkiniaa.scas.dto.*;
import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectAndUserIdDto;
import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectIdAndReportId;
import org.github.babkiniaa.scas.dto.typeForMap.MethodAndTypeAnalysis;
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
    public HashMap<String, String> getMethodMap() {
        HashMap<String, String> hashMap = agentServiceClient.getMethodMap();

        return hashMap;
    }

    @PostMapping("/report/create")
    public int createReport(@RequestBody ProjectDto projectDto) {

        return masterServiceClient.createReport(projectDto);
    }

    @PostMapping("report/get-owasp/")
    public ReportOWASPDto getReport(@RequestBody int idOWASP) {

        return masterServiceClient.getReport(idOWASP);
    }

    @PostMapping("/project/create")
    public int createProject(@RequestBody ProjectAndUserIdDto projectAndUserIdDto) {

        return masterServiceClient.createProject(projectAndUserIdDto);
    }

    @PostMapping("/project/get-projects")
    public List<ProjectDto> getProject(@RequestBody GetProjectDto projectsDto) {

        return masterServiceClient.getProject(projectsDto);
    }

    @GetMapping("report/get-all-owasp")
    public List<ReportOWASPDto> getAllReportsOwasp() {

        return masterServiceClient.getAllReportsOwasp();
    }

    @GetMapping("report/get-all-pmd")
    public List<ReportPMDDto> getAllReportsPmd() {

        return masterServiceClient.getAllReportsPmd();
    }

    @GetMapping("report/get-all-checkstyle")
    public List<ReportCheckStyleDto> getAllReportsCheckstyle() {

        return masterServiceClient.getAllReportsCheckstyle();
    }

    @GetMapping("report/get-all-spotbugs")
    public List<ReportSpotBugsDto> getAllReportsSpotBugs() {

        return masterServiceClient.getAllReportsSpotBugs();
    }

    @GetMapping("report/get-owasp/{id}")
    public ReportOWASPDto getReportOwasp(@PathVariable("id") int id) {

        return masterServiceClient.getReportOwasp(id);
    }

    @GetMapping("report/get-pmd/{id}")
    public ReportPMDDto getReportPMD(@PathVariable("id") int id) {

        return masterServiceClient.getReportPMD(id);
    }

    @GetMapping("report/get-checkstyle/{id}")
    public ReportCheckStyleDto getReportCheckstyle(@PathVariable("id") int id) {

        return masterServiceClient.getReportCheckstyle(id);
    }

    @GetMapping("report/get-spotbugs/{id}")
    public ReportSpotBugsDto getReportSpotBugs(@PathVariable("id") int id) {

        return masterServiceClient.getReportSpotBugs(id);
    }


    @GetMapping("project/get-project/{id}")
    public ProjectDto getProject(@PathVariable("id") int id) {

        return masterServiceClient.getProject(id);
    }


    @PostMapping("project/connecting-report-pmd")
    public ResponseEntity<?> connectionUserAndReportPmd(@RequestBody ProjectIdAndReportId projectIdAndReportId) {

        return masterServiceClient.connectionUserAndReportPmd(projectIdAndReportId);
    }

    @PostMapping("project/connecting-report-owasp")
    public ResponseEntity<?> connectionUserAndReport(@RequestBody ProjectIdAndReportId projectIdAndReportId) {

        return masterServiceClient.connectionUserAndReport(projectIdAndReportId);
    }

    @GetMapping("project/get-all")
    public List<ProjectDto> getAllProject() {

        return masterServiceClient.getAllProject();
    }


}
