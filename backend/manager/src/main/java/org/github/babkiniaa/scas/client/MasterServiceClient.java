package org.github.babkiniaa.scas.client;

import org.github.babkiniaa.scas.dto.ListReportDto;
import org.github.babkiniaa.scas.dto.Response.TaskInQueueDto;
import org.github.babkiniaa.scas.dto.project.*;
import org.github.babkiniaa.scas.dto.Response.ReportDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "master-service", url = "http://localhost:8082")
public interface MasterServiceClient {

    @PostMapping("/create/offline")
    long createReportOffline(@RequestBody ProjectIdAndReportId projectIdAndReportId);

    @PostMapping("/report/create")
    long createReport(@RequestBody AnalyserDto analyserDto);

    @PostMapping("/project/create")
    long createProject(@RequestBody ProjectCreateDto projectCreateDto);

    @GetMapping("report/status/{id}")
    String getStatus(@PathVariable("id") Long id);

    @GetMapping("report/get-reports/{id}")
    ReportDto getRep(@PathVariable("id") long projectId);

    @GetMapping("report/get-by-project/{id}")
    List<ListReportDto> findReportsByProjectId(@PathVariable("id") long projectId);

    @GetMapping("report/find/{id}")
    ReportDto getReport(@PathVariable("id") Long id);

    @PostMapping("/project/get-projects")
    List<ProjectDto> getProject(@RequestBody GetProjectAllDto projectsDto);

    @GetMapping("project/get-all")
    List<ProjectDto> getAllProject();

    @GetMapping("project/get-project/{id}")
    ProjectDto getProject(@PathVariable int id);

    @GetMapping("/agent/count-queue")
    int getCountQueue();

    @GetMapping("/agent/get-run-task")
    List<TaskInQueueDto> getRunTask();

    @PostMapping("/agent/task/ban/{id}")
    void banTask(@PathVariable("id") long taskId);

}
