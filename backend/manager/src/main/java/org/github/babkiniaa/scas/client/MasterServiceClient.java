package org.github.babkiniaa.scas.client;

import org.github.babkiniaa.scas.dto.*;
import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectAndUserIdDto;
import org.github.babkiniaa.scas.dto.ProjectAndId.ProjectIdAndReportId;
import org.github.babkiniaa.scas.dto.Response.ReportDto;
import org.github.babkiniaa.scas.dto.project.AnalyserDto;
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
    long createReport(@RequestBody AnalyserDto analyserDto);

    @PostMapping("/project/create")
    long createProject(@RequestBody ProjectAndUserIdDto projectAndUserIdDto);

    @GetMapping("report/status/{id}")
    String getStatus(@PathVariable("id") Long id);

    @GetMapping("/get-reports/{id}")
    ReportDto getRep(@PathVariable("id") long projectId);

    @GetMapping("report/find/{id}")
    ReportDto getReport(@PathVariable("id") Long id);

    @GetMapping("report/save/{id}")
    ReportDto saveReport(@PathVariable("id") Long id);

    @PostMapping("/project/get-projects")
    List<ProjectDto> getProject(@RequestBody GetProjectAllDto projectsDto);

    @GetMapping("project/get-all")
    List<ProjectDto> getAllProject();

    @GetMapping("project/get-project/{id}")
    ProjectDto getProject(@PathVariable int id);

}
