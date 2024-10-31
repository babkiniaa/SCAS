package org.github.babkiniaa.scas.Controller;

import lombok.RequiredArgsConstructor;
import org.apache.maven.shared.invoker.*;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.dto.ReportOWASPDto;
import org.github.babkiniaa.scas.dto.reportsDto.DependencyCustomDto;
import org.github.babkiniaa.scas.mapper.ReportCheckStyleMapper;
import org.github.babkiniaa.scas.mapper.ReportOWASPMapper;
import org.github.babkiniaa.scas.mapper.ReportPMDMapper;
import org.github.babkiniaa.scas.mapper.ReportSpotBugsMapper;
import org.github.babkiniaa.scas.parsers.CheckStyleParser;
import org.github.babkiniaa.scas.parsers.DependencyCheckParser;
import org.github.babkiniaa.scas.parsers.PmdParser;
import org.github.babkiniaa.scas.parsers.SpotBugsParser;
import org.github.babkiniaa.scas.utils.analysis.BinAnalysis;
import org.github.babkiniaa.scas.utils.analysis.StaticAnalysis;
import org.owasp.dependencycheck.dependency.Dependency;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class ChallengeController {

    private final DependencyCheckParser dependencyCheckParser;
    private final CheckStyleParser checkStyleParser;
    private final PmdParser pmdParser;
    private final SpotBugsParser spotBugsParser;
    private final ReportCheckStyleMapper reportCheckStyleMapper;
    private final ReportOWASPMapper reportOWASPMapper;
    private final ReportPMDMapper reportPMDMapper;
    private final ReportSpotBugsMapper reportSpotBugsMapper;

//    @GetMapping("/reports")
//    public List<Report> allReports() {
//        return reportService.findAll();
//    }
//
//    @GetMapping("/projects")
//    public List<Project> allProjects() {
//        return projectService.findAll();
//    }
//
//    @GetMapping("/report/get")
//    public Optional<Report> getReport(@RequestParam Integer id) {
//        return reportService.findById(id);
//    }
//
//    @PostMapping("/delete-reports")
//    public ResponseEntity<?> deleteReport(@RequestBody ReportIdDto reportIdDto) {
//        reportService.delete(reportIdDto.getId());
//        return ResponseEntity.ok("Удалил отчет");
//    }

    @PostMapping("/init")
    public ProjectDto start(@RequestBody List<String> checks, @RequestBody ProjectDto projectDto) throws IOException, InterruptedException {
//        reportCheckstyle();
        ReportOWASPDto reportOwasp = new ReportOWASPDto();
        reportOwasp.setReportList(reportOwasp());
        List<ReportOWASPDto> reportOWASPDto = projectDto.getReportOWASPS();
        reportOWASPDto.add(reportOwasp);
        projectDto.setReportOWASPS(reportOWASPDto);

//        reportSpotBugs();
//        reportPmd();

//        ReportDto reportDto = new ReportDto(projectDto.getNameProject());
//
//        Integer idReport = reportService.create(reportDto).getId();
//        projectDto.setReport(reportService.findById(idReport).get());
//        Integer idProject = projectService.create(projectDto, user.getId()).getId();
//        String username = authenticationFacade.getCurrentUserName();
//        User user = userService.findByUsername(username).get();
//        List<Project> projectList = user.getProjectList();
//        projectList.add(projectService.findById(idProject).get());
//        user.setProjectList(projectList);
//
//        ReportIdDto reportIdDto = new ReportIdDto(idReport, reportDto.getNameReport());
//        GitUtil.downloadUrl(projectDto.getUrl(), idProject);
//        for (String check : projectDto.getListOfChecks()) {
//            switch (check) {
//                case "owasp-start":
//                    try {
//                        String owaspRep = reportOwasp(idReport);
//                        reportService.updateOWASP(idReport, owaspRep);
//                    } catch (Exception e) {
//                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при выполнении Pmd: " + e.getMessage());
//                    }
//                    break;
//                case "pmd-start":
//                    try {
//                        String pmdRep = reportPmd(idReport);
//                        reportService.updatePmd(idReport, pmdRep);
//                    } catch (Exception e) {
//                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при выполнении Pmd: " + e.getMessage());
//                    }
//                    break;
//                case "checkstyle-start":
//                    try {
//                        String checkRep = reportCheckstyle(idReport);
//                        reportService.updateCheckStyle(idReport, checkRep);
//                    } catch (Exception e) {
//                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при выполнении Checkstyle: " + e.getMessage());
//                    }
//
//                    break;
//                case "spotbugs-start":
//                    try {
//                        String spotRep = reportSpotBugs(idReport);
//                        reportService.updateSpotbugs(idReport, spotRep);
//                    } catch (Exception e) {
//                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при выполнении SpotBugs: " + e.getMessage());
//                    }
//                    break;
//            }
//        }
//        DeleteFileUtil.deleteFile(idReport);
//
        return projectDto;
    }

    private String reportSpotBugs() {
        String report = "";
//        String patch = System.getProperty("user.dir") + "/backend/agent/target/spotbugs/" + reportId + "/spotbugsXml.xml";
//        System.setProperty("maven.home", System.getenv("M2_HOME"));
//        InvocationRequest request = new DefaultInvocationRequest();
//        String patchPom = System.getProperty("user.dir") + "/down" +  reportId;
//        request.setPomFile(new File(patchPom + "/pom.xml"));
//        request.setGoals(Collections.singletonList("compile"));
//        Invoker invoker = new DefaultInvoker();
//        invoker.execute(request);
//        BinAnalysis.spotbugs(System.getProperty("user.dir") + "/down/" + reportId);
//        report = spotBugsParser.parse(patch);
        return report;

    }

    private String reportPmd() {
        String report = "";
//        String patch = System.getProperty("user.dir") + "/backend/agent/target/pmd-res/" + reportId + "/pmd.xml";
//
//        StaticAnalysis.startPmd(String.valueOf(reportId));
//        report = pmdParser.parse(patch);
        return report;

    }

    private List<DependencyCustomDto> reportOwasp() throws IOException, InterruptedException {
        List<DependencyCustomDto> reportDto = null;
        List<Dependency> dependencies = null;
        dependencies = StaticAnalysis.startOWASP(" fgfg");
        reportDto = reportOWASPMapper.owaspToOwaspCustomList(dependencies);
//        String patch = System.getProperty("user.dir") + "/down/" + reportId;
//
//        StaticAnalysis.startOWASP(patch);
//        report = dependencyCheckParser.parse(patch);
        return reportDto;
    }


    private String reportCheckstyle() {
        String report = "";
//        String patch = System.getProperty("user.dir") + "/backend/agent/target/checkstyle-reports/" + reportId + "/checkstyle-result.xml";
//
//        StaticAnalysis.startCheckStyle(reportId.toString());
//        report = checkStyleParser.parse(patch);
        return report;
    }


}
