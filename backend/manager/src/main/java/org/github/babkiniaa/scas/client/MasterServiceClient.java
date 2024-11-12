package org.github.babkiniaa.scas.client;

import org.github.babkiniaa.scas.dto.*;
import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectAndUserIdDto;
import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectIdAndReportId;
import org.github.babkiniaa.scas.dto.project.GetProjectAllDto;
import org.github.babkiniaa.scas.dto.project.ProjectDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "master-service", url = "http://localhost:8082")
public interface MasterServiceClient {

    @PostMapping("/report/create")
    int createReport(@RequestBody StartAnalysisDto startAnalysisDto);

    @PostMapping("report/get-owasp/")
    ReportOWASPDto getReport(@RequestBody Integer idOWASP);

    @PostMapping("/project/create")
    int createProject(@RequestBody ProjectAndUserIdDto projectAndUserIdDto);

    @PostMapping("/project/get-projects")
    List<ProjectDto> getProject(@RequestBody GetProjectAllDto projectsDto);

    @GetMapping("report/get-all-owasp")
    List<ReportOWASPDto> getAllReportsOwasp();

    @GetMapping("report/get-all-pmd")
    List<ReportPMDDto> getAllReportsPmd();

    @GetMapping("report/get-all-checkstyle")
    List<ReportCheckStyleDto> getAllReportsCheckstyle();

    @GetMapping("report/get-all-spotbugs")
    List<ReportSpotBugsDto> getAllReportsSpotBugs();

    @GetMapping("report/get-owasp/{id}")
    ReportOWASPDto getReportOwasp(@PathVariable("id") int id);

    @GetMapping("report/get-pmd/{id}")
    ReportPMDDto getReportPMD(@PathVariable("id") int id);

    @GetMapping("report/get-checkstyle/{id}")
    ReportCheckStyleDto getReportCheckstyle(@PathVariable("id") int id);

    @GetMapping("report/get-spotbugs/{id}")
    ReportSpotBugsDto getReportSpotBugs(@PathVariable("id") int id);

    @PostMapping("project/connecting-report-owasp")
    ResponseEntity<?> connectionUserAndReport(@RequestBody ProjectIdAndReportId projectIdAndReportId);

    @PostMapping("project/connecting-report-pmd")
    ResponseEntity<?> connectionUserAndReportPmd(@RequestBody ProjectIdAndReportId projectIdAndReportId);

    @GetMapping("project/get-all")
    List<ProjectDto> getAllProject();

    @GetMapping("project/get-project/{id}")
    ProjectDto getProject(@PathVariable int id);

}
