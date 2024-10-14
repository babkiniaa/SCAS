package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.analysis.StaticAnalysis;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.dto.ReportIdDto;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.mapper.ReportMapper;
import org.github.babkiniaa.scas.service.ReportService;
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
    private final StaticAnalysis staticAnalysis;

    @GetMapping("/reports")
    public List<Report> allReports() {
        return reportService.findAll();
    }

    @GetMapping("/report/Get")
    public Optional<Report> getReport(@RequestParam Integer id){
        return reportService.findById(id);
    }

    @PostMapping("/checkstyle/start")
    public ResponseEntity<?> reportCheckstyle(@RequestBody ReportIdDto reportIdDto) throws Exception {
        String report = "";
        staticAnalysis.startCheckStyle(reportIdDto.getId().toString());
        reportService.updateCheckStyle(reportIdDto.getId(), report);
        return ResponseEntity.ok(" Checkstyle отработал ");
    }

}
