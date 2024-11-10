package org.github.babkiniaa.scas.Controller;

import lombok.RequiredArgsConstructor;
import net.sourceforge.pmd.reporting.RuleViolation;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.dto.Request.RegisterTaskDto;
import org.github.babkiniaa.scas.dto.oldReports.ReportOWASPDto;
import org.github.babkiniaa.scas.dto.oldReports.ReportPMDDto;
import org.github.babkiniaa.scas.dto.reportsDto.DependencyCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.RuleViolationCustomDto;
import org.github.babkiniaa.scas.dto.typeForMap.MethodAndTypeAnalysis;
import org.github.babkiniaa.scas.mapper.*;
import org.github.babkiniaa.scas.service.TaskService;
import org.github.babkiniaa.scas.utils.DeleteFileUtil;
import org.github.babkiniaa.scas.utils.GitUtil;
import org.github.babkiniaa.scas.utils.analysis.StaticAnalysis;
import org.owasp.dependencycheck.dependency.Dependency;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class ChallengeController {

    private static ReportCheckStyleMapper reportCheckStyleMapper;
    private static ReportOWASPMapper reportOWASPMapper;
    private static ReportPMDMapper reportPMDMapper;
    private static ReportSpotBugsMapper reportSpotBugsMapper;

    @PostMapping("/start/queue")
    public ResponseEntity<?> initTask(RegisterTaskDto registerTaskDto){
        JobDetail j = JobBuilder.newJob().build();
        return ResponseEntity.ok("ok ");
    }

    public void HYIS(){
        System.out.println("SPASIBA");
    }

//    @PostMapping("/start")
//    public ProjectDto start(@RequestBody ProjectDto projectDto) {
//
//        String dir = System.getProperty("user.dir") + "/down";
//        try {
//            GitUtil.cloneRepository(projectDto.getUrl(), dir);
//        } catch (GitAPIException e) {
//            throw new RuntimeException(e);
//        }
//        for (String run : projectDto.getNeedReports()) {
//            projectDto = (ProjectDto) methodMap.get(run).getFunction().apply(projectDto);
//        }
//        DeleteFileUtil.deleteDir(new File(dir));
//
//        return projectDto;
//    }
//
//    @GetMapping("/get-hashmap")
//    public HashMap<String, String> getMethodMap(){
//        HashMap<String, String> hashMap = new HashMap<>();
//
//        for(String  m: methodMap.keySet()){
//            hashMap.put(m, methodMap.get(m).getType());
//        }
//
//        return hashMap;
//    }
//
//    private String reportSpotBugs() {
//        String report = "";
//
//        return report;
//
//    }
//
//    private ProjectDto reportPmd(ProjectDto projectDto) {
//        List<RuleViolationCustomDto> violationCustomDtos = new ArrayList<>();
//        List<RuleViolation> ruleViolationList = new ArrayList<>();
//        String dir = System.getProperty("user.dir") + "/down";
//
//        try {
//            ruleViolationList = StaticAnalysis.startPmd(dir);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        violationCustomDtos = reportPMDMapper.PMDtoDTO(ruleViolationList);
//        ReportPMDDto reportPMD = new ReportPMDDto();
//        reportPMD.setReportList(violationCustomDtos);
//        List<ReportPMDDto> reportPMDDto = new ArrayList<>();
//
//        if (projectDto.getReportPMDS() != null) {
//            reportPMDDto = projectDto.getReportPMDS();
//        }
//        reportPMDDto.add(reportPMD);
//        projectDto.setReportPMDS(reportPMDDto);
//
//        return projectDto;
//
//    }
//
//    private ProjectDto reportOwasp(ProjectDto projectDto) {
//        List<DependencyCustomDto> dependencyCustomDtos;
//        List<Dependency> dependencies = new ArrayList<>();
//        String dir = System.getProperty("user.dir") + "/down";
//        dependencies = StaticAnalysis.startOWASP(dir);
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
//
//        return projectDto;
//    }
//
//    private ProjectDto reportCheckstyle(ProjectDto projectDto) {
//        String report = "";
//        String dir = System.getProperty("user.dir") + "/down";
//
//        return projectDto;
//    }
//
//    private ProjectDto reportSpotBugs(ProjectDto projectDto) {
//        String report = "";
//        String dir = System.getProperty("user.dir") + "/down";
//
//        return projectDto;
//    }

}
