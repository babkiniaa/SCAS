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

    @PostMapping("/owasp-start")
    public ResponseEntity<?> reportOwasp(@RequestBody ReportIdDto reportidDto) throws IOException, InterruptedException {
        String report = "";
        String patch = System.getProperty("user.dir") + "/down/" + reportidDto.getId();
        staticAnalysis.startOWASP(patch);
        reportService.updateOWASP(reportidDto.getId(), report);
        return ResponseEntity.ok("OWASP отработал успешно");
    }

}
