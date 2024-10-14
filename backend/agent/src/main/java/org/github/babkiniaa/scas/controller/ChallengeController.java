package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.mapper.ReportMapper;
import org.github.babkiniaa.scas.service.ReportService;
import org.github.babkiniaa.scas.textReader.GitStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

@RestController
@RequiredArgsConstructor
public class ChallengeController {

    private final ReportService reportService;
    private final ExecutorService executorService;;
    private final ReportMapper reportMapper;
    private final GitStatus gitStatus;


    @GetMapping("/reports")
    public List<Report> allReports() {
        return reportService.findAll();
    }

    @GetMapping("/report/Get")
    public Optional<Report> getReport(@RequestParam Integer id){
        return reportService.findById(id);
    }

    @PostMapping("/report-download")
    public Integer downloadUrl(@RequestBody ProjectDto projectDto) throws GitAPIException {
        String currentDir = System.getProperty("user.dir") + "/backend/agent/src/main/java/";
        String currentDirUser = System.getProperty("user.dir") + "/down/";
        ReportDto reportDto = new ReportDto(projectDto.getNameProject());
        gitStatus.cloneRepository(projectDto.getUrl(), currentDirUser);
        gitStatus.cloneRepository(projectDto.getUrl(), currentDir);
        return reportService.create(reportDto).getId();
    }
}
