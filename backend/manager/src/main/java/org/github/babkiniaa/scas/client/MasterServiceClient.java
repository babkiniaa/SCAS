package org.github.babkiniaa.scas.client;

import edu.umd.cs.findbugs.Project;
import org.github.babkiniaa.scas.dto.*;
import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectAndUserIdDto;
import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectIdAndReportId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "master-service", url = "http://localhost:8082")
public interface MasterServiceClient {

    @PostMapping("/report/create")
    ResponseEntity<?> createReport(@RequestBody ProjectDto reportDto);

    @PostMapping("report/get-owasp/")
    ReportOWASPDto getReport(@RequestBody int idOWASP);

    @PostMapping("/project/create")
    int createProject(@RequestBody ProjectAndUserIdDto projectAndUserIdDto);

    @PostMapping("/project/get-reports")
    List<ProjectDto> getProject(@RequestBody GetProjectDto projectsDto);

    @GetMapping("report/get-all-owasp")
    List<ReportOWASPDto> getAllReportsOwasp();

    @GetMapping("report/get-all-pmd")
    List<ReportPMDDto> getAllReportsPmd();

    @GetMapping("report/get-all-checkstyle")
    List<ReportCheckStyleDto> getAllReportsCheckstyle();

    @GetMapping("report/get-all-spotbugs")
    List<ReportSpotBugsDto> getAllReportsSpotBugs();

    @PostMapping("project/connecting-report")
    ResponseEntity<?> connectionUserAndReport(@RequestBody ProjectIdAndReportId projectIdAndReportId);

    @PostMapping("project/get-all")
    List<ProjectDto> getAllProject();
}
