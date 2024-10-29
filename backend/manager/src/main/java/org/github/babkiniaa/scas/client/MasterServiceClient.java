package org.github.babkiniaa.scas.client;

import org.github.babkiniaa.scas.dto.GetProjectDto;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.dto.ReportOWASPDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "master-service", url = "http://localhost:8082")
public interface MasterServiceClient {
    @PostMapping("/report/create")
    ResponseEntity<?> createReport(@RequestBody ProjectDto reportDto, @RequestBody int projectId);

    @PostMapping("report/get-owasp/")
    ReportOWASPDto getReport(@RequestBody int idOWASP);

    @PostMapping("/project/create")
    int createProject(@RequestBody ProjectDto projectDto, @RequestBody int userId);

    @PostMapping("/project/get-reports")
    List<ProjectDto> getProject(@RequestBody GetProjectDto projectsDto);


}
