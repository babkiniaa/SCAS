package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;

import org.github.babkiniaa.scas.analysis.BinAnalysis;
import org.github.babkiniaa.scas.analysis.StaticAnalysis;
import org.eclipse.jgit.api.errors.GitAPIException;

import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.dto.ReportIdDto;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.service.ReportService;
import org.github.babkiniaa.scas.textReader.GitStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
//    private final ExecutorService executorService;
    private final BinAnalysis binAnalysis;
    private final StaticAnalysis staticAnalysis;
    private final GitStatus gitStatus;

    @GetMapping("/reports")
    public List<Report> allReports() {
        return reportService.findAll();
    }

    @GetMapping("/report/Get")
    public Optional<Report> getReport(@RequestParam Integer id) {
        return reportService.findById(id);
    }

    @SneakyThrows
    @PostMapping("/spotbugs-start")
    public ResponseEntity<?> reportSpotBugs(@RequestBody ReportIdDto reportIdDto) throws IOException, InterruptedException {
        String report = "";
        String patch = System.getProperty("user.dir") + "/down/" + reportIdDto.getId();
        System.setProperty("maven.home", System.getenv("M2_HOME"));
        InvocationRequest request = new DefaultInvocationRequest();

        request.setPomFile(new File(patch + "\\pom.xml"));
        request.setGoals(Collections.singletonList("compile"));
        Invoker invoker = new DefaultInvoker();
        invoker.execute(request);
        binAnalysis.spotbugs(patch);
        reportService.updateSpotbugs(reportIdDto.getId(), report);
        return ResponseEntity.ok("spotBugs отработал");
    }

    @PostMapping("/pmd-start")
    public ResponseEntity<?> reportPmd(@RequestBody ReportIdDto reportIdDto) throws Exception {
        String report = "";
        staticAnalysis.startPmd(reportIdDto.getId().toString());
        reportService.updatePmd(reportIdDto.getId(), report);
        return ResponseEntity.ok(" OWASP");
    }

    @PostMapping("/owasp-start")
    public ResponseEntity<?> reportOwasp(@RequestBody ReportIdDto reportIdDto) throws IOException, InterruptedException {
        String report = "";
        String patch = System.getProperty("user.dir") + "/down/" + reportIdDto.getId();
        staticAnalysis.startOWASP(patch);
        reportService.updateOWASP(reportIdDto.getId(), report);
        return ResponseEntity.ok("OWASP отработал успешно");
    }

    @PostMapping("/checkstyle-start")
    public ResponseEntity<?> reportCheckstyle(@RequestBody ReportIdDto reportIdDto) throws Exception {
        String report = "";
        staticAnalysis.startCheckStyle(reportIdDto.getId().toString());
        reportService.updateCheckStyle(reportIdDto.getId(), report);
        return ResponseEntity.ok(" Checkstyle отработал ");

    }

    @PostMapping("/report-download")
    public Integer downloadUrl(@RequestBody ProjectDto projectDto) throws GitAPIException {
        ReportDto reportDto = new ReportDto(projectDto.getNameProject());
        Integer idReport = reportService.create(reportDto).getId();
        String currentDir = System.getProperty("user.dir") + "/backend/agent/src/main/java/" + idReport;
        String currentDirUser = System.getProperty("user.dir") + "/down/" + idReport;
        gitStatus.cloneRepository(projectDto.getUrl(), currentDirUser);
        gitStatus.cloneRepository(projectDto.getUrl(), currentDir);
        return idReport;
    }

}
