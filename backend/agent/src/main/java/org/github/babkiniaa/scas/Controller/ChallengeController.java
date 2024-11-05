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

    private static ReportCheckStyleMapper reportCheckStyleMapper;
    private static ReportOWASPMapper reportOWASPMapper;
    private static ReportPMDMapper reportPMDMapper;
    private static ReportSpotBugsMapper reportSpotBugsMapper;
    private static final HashMap<String, Function<ProjectDto, ?>> methodMap = new HashMap<>();

    {
        methodMap.put("OWASP", (Function<ProjectDto, ProjectDto>) ChallengeController::reportOwasp);
        methodMap.put("PMD", (Function<ProjectDto, ProjectDto>) ChallengeController::reportPmd);
        methodMap.put("CheckStyle", (Function<ProjectDto, ProjectDto>) ChallengeController::reportCheckstyle);
        methodMap.put("SpotBugs", (Function<ProjectDto, ProjectDto>) ChallengeController::reportSpotBugs);

    }

    @PostMapping("/init")
    public ProjectDto start(@RequestBody ProjectDto projectDto) {

        String dir = System.getProperty("user.dir") + "/down";
        try {
            GitUtil.cloneRepository(projectDto.getUrl(), dir);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        projectDto = (ProjectDto) methodMap.get("OWASP").apply(projectDto);
        projectDto = (ProjectDto) methodMap.get("PMD").apply(projectDto);
        DeleteFileUtil.deleteDir(new File(dir));

        return projectDto;
    }

    private String reportSpotBugs() {
        String report = "";

        return report;

    }

    private static ProjectDto reportPmd(ProjectDto projectDto) {
        List<RuleViolationCustomDto> violationCustomDtos = new ArrayList<>();
        List<RuleViolation> ruleViolationList = new ArrayList<>();
        String dir = System.getProperty("user.dir") + "/down";

        try {
            ruleViolationList = StaticAnalysis.startPmd(dir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        violationCustomDtos = reportPMDMapper.PMDtoDTO(ruleViolationList);
        ReportPMDDto reportPMD = new ReportPMDDto();
        reportPMD.setReportList(violationCustomDtos);
        List<ReportPMDDto> reportPMDDto = new ArrayList<>();

        if (projectDto.getReportPMDS() != null) {
            reportPMDDto = projectDto.getReportPMDS();
        }
        reportPMDDto.add(reportPMD);
        projectDto.setReportPMDS(reportPMDDto);

        return projectDto;

    }

    private static ProjectDto reportOwasp(ProjectDto projectDto) {
        List<DependencyCustomDto> dependencyCustomDtos;
        List<Dependency> dependencies = new ArrayList<>();
        String dir = System.getProperty("user.dir") + "/down";
        dependencies = StaticAnalysis.startOWASP(dir);
        dependencyCustomDtos = reportOWASPMapper.owaspToOwaspCustomList(dependencies);
        ReportOWASPDto reportOwasp = new ReportOWASPDto();
        reportOwasp.setReportList(dependencyCustomDtos);
        List<ReportOWASPDto> reportOWASPDto = new ArrayList<>();

        if (projectDto.getReportOWASPS() != null) {
            reportOWASPDto = projectDto.getReportOWASPS();
        }
        reportOWASPDto.add(reportOwasp);
        projectDto.setReportOWASPS(reportOWASPDto);

        return projectDto;
    }

    private static ProjectDto reportCheckstyle(ProjectDto projectDto) {
        String report = "";
        String dir = System.getProperty("user.dir") + "/down";

        return projectDto;
    }

    private static ProjectDto reportSpotBugs(ProjectDto projectDto) {
        String report = "";
        String dir = System.getProperty("user.dir") + "/down";

        return projectDto;
    }

}
