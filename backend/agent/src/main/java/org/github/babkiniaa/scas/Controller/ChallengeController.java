package org.github.babkiniaa.scas.Controller;

import com.puppycrawl.tools.checkstyle.api.Violation;
import edu.umd.cs.findbugs.BugInstance;
import lombok.RequiredArgsConstructor;
import net.sf.saxon.trans.SymbolicName;
import net.sourceforge.pmd.reporting.RuleViolation;
import org.checkerframework.checker.units.qual.K;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.dto.ReportOWASPDto;
import org.github.babkiniaa.scas.dto.ReportPMDDto;
import org.github.babkiniaa.scas.dto.reportsDto.DependencyCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.RuleViolationCustomDto;
import org.github.babkiniaa.scas.mapper.*;
import org.github.babkiniaa.scas.parsers.CheckStyleParser;
import org.github.babkiniaa.scas.parsers.DependencyCheckParser;
import org.github.babkiniaa.scas.parsers.PmdParser;
import org.github.babkiniaa.scas.parsers.SpotBugsParser;
import org.github.babkiniaa.scas.utils.DeleteFileUtil;
import org.github.babkiniaa.scas.utils.GitUtil;
import org.github.babkiniaa.scas.utils.analysis.StaticAnalysis;
import org.h2.table.FunctionTable;
import org.owasp.dependencycheck.dependency.Dependency;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class ChallengeController {

    private final ReportCheckStyleMapper reportCheckStyleMapper;
    private final ReportOWASPMapper reportOWASPMapper;
    private final ReportPMDMapper reportPMDMapper;
    private final ReportSpotBugsMapper reportSpotBugsMapper;
    private static final HashMap<String, Function<ProjectDto, ?>> methodMap = new HashMap<>();

    {
        methodMap.put("OWASP", (Function<ProjectDto, List<Dependency>>) ChallengeController::reportOwasp);
        methodMap.put("PMD", (Function<ProjectDto, List<RuleViolation>>) ChallengeController::reportPmd);
        methodMap.put("CheckStyle", (Function<ProjectDto, List<Violation>>) ChallengeController::reportCheckstyle);
        methodMap.put("SpotBugs", (Function<ProjectDto, List<BugInstance>>) ChallengeController::reportSpotBugs);
    }

    @PostMapping("/init")
    public ProjectDto start(@RequestBody ProjectDto projectDto) {
        String dir = System.getProperty("user.dir") + "/down";
        try {
            GitUtil.cloneRepository(projectDto.getUrl(), dir);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        List<ReportOWASPDto> reportOWASPDtos = projectDto.getReportOWASPS();
        if(reportOWASPDtos == null){
            reportOWASPDtos = new ArrayList<>();
        }
        ReportOWASPDto reportOWASPDto = new ReportOWASPDto();
        List<Dependency> dependency = (List<Dependency>) methodMap.get("OWASP").apply(projectDto);
        List<DependencyCustomDto> dependencyCustomDto = reportOWASPMapper.owaspToOwaspCustomList(dependency);
        reportOWASPDto.setReportList(dependencyCustomDto);
        reportOWASPDtos.add(reportOWASPDto);
        projectDto.setReportOWASPS(reportOWASPDtos);
        DeleteFileUtil.deleteDir(new File(dir));
        return projectDto;
    }

    private String reportSpotBugs() {
        String report = "";
        return report;

    }

    private static List<RuleViolation> reportPmd(ProjectDto projectDto) {
        List<RuleViolationCustomDto> violationCustomDtos = new ArrayList<>();
        List<RuleViolation> ruleViolationList = new ArrayList<>();
        String dir = System.getProperty("user.dir") + "/down";

        try {
            ruleViolationList = StaticAnalysis.startPmd(dir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        violationCustomDtos = reportPMDMapper.PMDtoDTO(ruleViolationList);
//        ReportPMDDto reportPMD = new ReportPMDDto();
//        reportPMD.setReportList(violationCustomDtos);
//        List<ReportPMDDto> reportPMDDto = new ArrayList<>();
//        if (projectDto.getReportPMDS() != null) {
//            reportPMDDto = projectDto.getReportPMDS();
//        }
//        reportPMDDto.add(reportPMD);
//        projectDto.setReportPMDS(reportPMDDto);

        return ruleViolationList;

    }

    private static List<Dependency> reportOwasp(ProjectDto projectDto) {
        List<DependencyCustomDto> dependencyCustomDtos;
        List<Dependency> dependencies = new ArrayList<>();
        String dir = System.getProperty("user.dir") + "/down";
        dependencies = StaticAnalysis.startOWASP(dir);
//        dependencyCustomDtos = reportOWASPMapper.owaspToOwaspCustomList(dependencies);
//        ReportOWASPDto reportOwasp = new ReportOWASPDto();
//        reportOwasp.setReportList(dependencyCustomDtos);
//        List<ReportOWASPDto> reportOWASPDto = new ArrayList<>();
//
//        if (projectDto.getReportOWASPS() != null) {
//            reportOWASPDto = projectDto.getReportOWASPS();
//        }
//        reportOWASPDto.add(reportOwasp);
//        projectDto.setReportOWASPS(reportOWASPDto);

        return dependencies;
    }

    private static List<Violation> reportCheckstyle(ProjectDto projectDto) {
        String report = "";
        String dir = System.getProperty("user.dir") + "/down";

        return null;
    }

    private static List<BugInstance> reportSpotBugs(ProjectDto projectDto) {
        String report = "";
        String dir = System.getProperty("user.dir") + "/down";

        return null;
    }

}
