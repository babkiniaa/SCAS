package org.example.controller;

import org.example.Mapper.ReportMapper;
import org.example.dto.ReportDto;
import org.example.entity.Report;
import org.example.repository.*;
import org.example.service.ReportService;
import org.example.textReader.Manager;
import org.springframework.http.HttpStatus;
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
public class ChallengeController {

    private final Manager manager;
    private final ReportService reportService;
    private final ExecutorService executorService;;
    private final ReportMapper reportMapper;
    private final Report report;

    public ChallengeController(Manager manager, ExecutorService executorService, ReportService reportService, ReportMapper reportMapper, Report report) {
        this.manager = manager;
        this.executorService = executorService;
        this.reportService = reportService;
        this.reportMapper = reportMapper;
        this.report = report;
    }

    @GetMapping("/reports")
    public List<Report> allReports() {
        return reportService.findAll();
    }

    @GetMapping("/reportGet")
    public Optional<Report> getReport(@RequestParam Integer id){
        return reportService.findById(id);
    }


    //    Многопоточная версия
    @PostMapping("/api/main")
    public ResponseEntity<String> CallWork(@RequestParam String url, @RequestParam String name) {

        executorService.submit(() -> {
            try {
                manager.setUrl(url);
                manager.start();
                ReportDto reportDto = new ReportDto(name, manager.getRepOWASP(), manager.getRepPMD(), manager.getRepStyle(), manager.getRepSpotBug());
                reportService.create(reportMapper.reportToEntity(reportDto));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return ResponseEntity.ok("Работаем в фоне");
    }

}
