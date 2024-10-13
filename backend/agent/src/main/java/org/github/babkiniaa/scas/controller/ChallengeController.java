package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.mapper.ReportMapper;
import org.github.babkiniaa.scas.service.ReportService;
import org.github.babkiniaa.scas.textReader.GitStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/report/download/url")
    public Integer downloadUrl(@RequestParam String nameReport, @RequestParam String url) throws GitAPIException {
        //ТУТ НАДО ПРИБАВИТЬ ID ЕЩЕ ЮЗЕРА, НО Я ЗАБЫЛ КАК
        ReportDto reportDto = new ReportDto(nameReport);
        String currentDir = System.getProperty("user.dir") + "/backend/agent/src/main/java/";
        String currentDirUser = System.getProperty("user.dir") + "/down/" ;
        gitStatus.cloneRepository(url, currentDirUser);
        gitStatus.cloneRepository(url, currentDir);
        return reportService.create(reportDto).getId();
    }
}
