package org.github.babkiniaa.scas.Controller;

import lombok.RequiredArgsConstructor;
import net.sourceforge.pmd.reporting.RuleViolation;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.dto.ReportOWASPDto;
import org.github.babkiniaa.scas.dto.ReportPMDDto;
import org.github.babkiniaa.scas.dto.reportsDto.DependencyCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.RuleViolationCustomDto;
import org.github.babkiniaa.scas.mapper.ReportCheckStyleMapper;
import org.github.babkiniaa.scas.mapper.ReportOWASPMapper;
import org.github.babkiniaa.scas.mapper.ReportPMDMapper;
import org.github.babkiniaa.scas.mapper.ReportSpotBugsMapper;
import org.github.babkiniaa.scas.parsers.CheckStyleParser;
import org.github.babkiniaa.scas.parsers.DependencyCheckParser;
import org.github.babkiniaa.scas.parsers.PmdParser;
import org.github.babkiniaa.scas.parsers.SpotBugsParser;
import org.github.babkiniaa.scas.utils.DeleteFileUtil;
import org.github.babkiniaa.scas.utils.GitUtil;
import org.github.babkiniaa.scas.utils.analysis.StaticAnalysis;
import org.owasp.dependencycheck.dependency.Dependency;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.util.ArrayList;
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

    @PostMapping("/init")
    public ProjectDto start(@RequestBody ProjectDto projectDto) {
        String dir = System.getProperty("user.dir") + "/down";
        try {
            GitUtil.cloneRepository(projectDto.getUrl(), dir);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        ReportOWASPDto reportOwasp = new ReportOWASPDto();
        reportOwasp.setReportList(reportOwasp(dir));
        List<ReportOWASPDto> reportOWASPDto = new ArrayList<>();
        if (projectDto.getReportOWASPS() != null) {
            reportOWASPDto = projectDto.getReportOWASPS();
        }
        reportOWASPDto.add(reportOwasp);
        projectDto.setReportOWASPS(reportOWASPDto);

        ReportPMDDto reportPMD = new ReportPMDDto();
        reportPMD.setReportList(reportPmd(dir));
        List<ReportPMDDto> reportPMDDto = new ArrayList<>();
        if (projectDto.getReportPMDS() != null) {
            reportPMDDto = projectDto.getReportPMDS();
        }
        reportPMDDto.add(reportPMD);
        projectDto.setReportPMDS(reportPMDDto);

        DeleteFileUtil.deleteDir(new File(dir));
        return projectDto;
    }

    private String reportSpotBugs() {
        String report = "";
        return report;

    }

    private List<RuleViolationCustomDto> reportPmd(String dir) {
        List<RuleViolationCustomDto> violationCustomDtos = new ArrayList<>();
        List<RuleViolation> ruleViolationList = new ArrayList<>();

        try {
            ruleViolationList = StaticAnalysis.startPmd(dir);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        violationCustomDtos = reportPMDMapper.PMDtoDTO(ruleViolationList);
        return violationCustomDtos;

    }

    private List<DependencyCustomDto> reportOwasp(String dir) {
        List<DependencyCustomDto> dependencyCustomDtos = new ArrayList<>();
        List<Dependency> dependencies = new ArrayList<>();

        dependencies = StaticAnalysis.startOWASP(dir);
        dependencyCustomDtos = reportOWASPMapper.owaspToOwaspCustomList(dependencies);
        return dependencyCustomDtos;
    }


    private String reportCheckstyle() {
        String report = "";
        return report;
    }


}
