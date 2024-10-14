package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.github.babkiniaa.scas.analysis.BinAnalysis;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.mapper.ReportMapper;
import org.github.babkiniaa.scas.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

@RestController
@RequiredArgsConstructor
public class ChallengeController {

    private final ReportService reportService;
    private final ExecutorService executorService;
    private final BinAnalysis  binAnalysis;
    private final ReportMapper reportMapper;


    @GetMapping("/reports")
    public List<Report> allReports() {
        return reportService.findAll();
    }

    @GetMapping("/report/Get")
    public Optional<Report> getReport(@RequestParam Integer id) {
        return reportService.findById(id);
    }

    @SneakyThrows
    @PostMapping("/spotbugs/start")
    public ResponseEntity<?> reportSpotBugs(@RequestParam String patch, @RequestParam Integer id) throws IOException, InterruptedException {
        String report = "";
        System.setProperty("maven.home", System.getenv("M2_HOME"));
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(patch + "\\pom.xml"));
        request.setGoals(Collections.singletonList("compile"));
        Invoker invoker = new DefaultInvoker();
        invoker.execute(request);
        binAnalysis.spotbugs(patch);
        reportService.updateSpotbugs(id, report);
        return (ResponseEntity<?>) ResponseEntity.ok();
    }


}
