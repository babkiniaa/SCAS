package org.github.babkiniaa.scas.controllers;

import lombok.RequiredArgsConstructor;

import org.apache.maven.shared.invoker.*;

import org.eclipse.jgit.api.errors.GitAPIException;

import org.github.babkiniaa.scas.service.ProjectService;
import org.github.babkiniaa.scas.utils.analysis.BinAnalysis;
import org.github.babkiniaa.scas.utils.analysis.StaticAnalysis;
import org.github.babkiniaa.scas.utils.DeleteFileUtil;
import org.github.babkiniaa.scas.utils.GitUtil;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.dto.ReportIdDto;
import org.github.babkiniaa.scas.entity.Report;

import org.github.babkiniaa.scas.parsers.CheckStyleParser;
import org.github.babkiniaa.scas.parsers.DependencyCheckParser;
import org.github.babkiniaa.scas.parsers.PmdParser;
import org.github.babkiniaa.scas.parsers.SpotBugsParser;
import org.github.babkiniaa.scas.service.ReportService;


import org.springframework.http.HttpStatus;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class ChallengeController {

    private final ReportService reportService;
    private final ProjectService projectService;

    private final DependencyCheckParser dependencyCheckParser;
    private final CheckStyleParser checkStyleParser;
    private final PmdParser pmdParser;
    private final SpotBugsParser spotBugsParser;

    @GetMapping("/reports")
    public List<Report> allReports() {
        return reportService.findAll();
    }

    @GetMapping("/report/Get")
    public Optional<Report> getReport(@RequestParam Integer id) {
        return reportService.findById(id);
    }

    @PostMapping("/delete-reports")
    public ResponseEntity<?> deleteReport(@RequestBody ReportIdDto reportIdDto) {
        reportService.delete(reportIdDto.getId());
        return ResponseEntity.ok("Удалил отчет");
    }

    @PostMapping("/init")
    public ResponseEntity<?> start(@RequestBody ProjectDto projectDto) throws Exception {
        ReportDto reportDto = new ReportDto(projectDto.getNameProject());
        
        if (projectService.findByUrl(projectDto.getUrl()).isEmpty()) {
            Integer idProject = projectService.create(projectDto).getId();
        }
        Integer idReport = reportService.create(reportDto).getId();
        ReportIdDto reportIdDto = new ReportIdDto(idReport, reportDto.getNameReport());

        downloadUrl(projectDto);
        for (String check : projectDto.getListOfChecks()) {
            switch (check) {
                case "owasp-start":
                    try {
                        reportOwasp(reportIdDto);
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при выполнении Pmd: " + e.getMessage());
                    }
                    break;
                case "pmd-start":
                    try {
                        reportPmd(reportIdDto);
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при выполнении Pmd: " + e.getMessage());
                    }
                    break;
                case "checkstyle-start":
                    try {
                        reportCheckstyle(reportIdDto);
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при выполнении Checkstyle: " + e.getMessage());
                    }

                    break;
                case "spotbugs-start":
                    try {
                        reportSpotBugs(reportIdDto);
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при выполнении SpotBugs: " + e.getMessage());
                    }
                    break;
            }
        }
        deleteFile(reportIdDto);

        return ResponseEntity.ok("init отработал");
    }

    private void reportSpotBugs(ReportIdDto reportIdDto) throws MavenInvocationException, XMLStreamException {
        String report;
        String patch = System.getProperty("user.dir") + "/backend/agent/target/spotbugs/" + reportIdDto.getId() + "/spotbugsXml.xml";
        System.setProperty("maven.home", System.getenv("M2_HOME"));
        InvocationRequest request = new DefaultInvocationRequest();
        String patchPom = System.getProperty("user.dir") + "/down" +  reportIdDto.getId();
        request.setPomFile(new File(patchPom + "/pom.xml"));
        request.setGoals(Collections.singletonList("compile"));
        Invoker invoker = new DefaultInvoker();
        invoker.execute(request);
        BinAnalysis.spotbugs(System.getProperty("user.dir") + "/down/" + reportIdDto.getId());
        report = spotBugsParser.parse(patch);
        reportService.updateSpotbugs(reportIdDto.getId(), report);
    }

    private void reportPmd(ReportIdDto reportIdDto) throws Exception {
        String report;
        String patch = System.getProperty("user.dir") + "/backend/agent/target/pmd-res/" + reportIdDto.getId() + "/pmd.xml";

        StaticAnalysis.startPmd(reportIdDto.getId().toString());
        report = pmdParser.parse(patch);
        reportService.updatePmd(reportIdDto.getId(), report);
    }

    private void reportOwasp(ReportIdDto reportIdDto) throws IOException, InterruptedException {
        String report;
        String patch = System.getProperty("user.dir") + "/down/" + reportIdDto.getId();

        StaticAnalysis.startOWASP(patch);
        report = dependencyCheckParser.parse(patch);
        reportService.updateOWASP(reportIdDto.getId(), report);
    }


    private void reportCheckstyle(ReportIdDto reportIdDto) throws Exception {
        String report;
        String patch = System.getProperty("user.dir") + "/backend/agent/target/checkstyle-reports/" + reportIdDto.getId() + "/checkstyle-result.xml";

        StaticAnalysis.startCheckStyle(reportIdDto.getId().toString());
        report = checkStyleParser.parse(patch);
        reportService.updateCheckStyle(reportIdDto.getId(), report);
    }

    private void downloadUrl(ProjectDto projectDto) throws GitAPIException {
        ReportDto reportDto = new ReportDto(projectDto.getNameProject());
        Integer idReport = reportService.create(reportDto).getId();
        String currentDir = System.getProperty("user.dir") + "/backend/agent/src/main/java/" + idReport;
        String currentDirUser = System.getProperty("user.dir") + "/down/" + idReport;

        GitUtil.cloneRepository(projectDto.getUrl(), currentDirUser);
        GitUtil.cloneRepository(projectDto.getUrl(), currentDir);
    }

    private void deleteFile(ReportIdDto reportIdDto) throws IOException {
        String currentDir = System.getProperty("user.dir") + "/backend/agent/src/main/java/" + reportIdDto.getId();
        String currentDirUser = System.getProperty("user.dir") + "/down/" + reportIdDto.getId();

        DeleteFileUtil.del(currentDir, currentDirUser);
    }

}
