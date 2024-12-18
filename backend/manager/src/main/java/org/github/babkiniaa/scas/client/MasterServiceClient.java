package org.github.babkiniaa.scas.client;

import org.github.babkiniaa.scas.dto.ListReportDto;
import org.github.babkiniaa.scas.dto.Response.TaskInQueueDto;
import org.github.babkiniaa.scas.dto.project.*;
import org.github.babkiniaa.scas.dto.Response.ReportDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "master-service", url = "http://localhost:8082")
public interface MasterServiceClient {

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
    List<ProjectDtoAll> getAllProject(@RequestParam int page, @RequestParam String name);

    @GetMapping("project/get-project/{id}")
    ProjectDto getProject(@PathVariable int id);

    @GetMapping("/agent/count-queue")
    int getCountQueue();

    @GetMapping("/agent/get-run-task")
    List<TaskInQueueDto> getRunTask();

    @PostMapping("/agent/task/ban/{id}")
    void banTask(@PathVariable("id") long taskId);

}
