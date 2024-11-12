package org.github.babkiniaa.scas.controller;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.Mapper.*;
import org.github.babkiniaa.scas.client.AgentServiceClient;
import org.github.babkiniaa.scas.dto.*;
import org.github.babkiniaa.scas.dto.project.ProjectDto;
import org.github.babkiniaa.scas.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/report")
@CrossOrigin(origins = "http://localhost:9000")
@RestController
public class ReportController {

    private final ProjectService projectService;
    private final ReportSpotBugsService reportSpotBugsService;
    private final ReportOWASPService reportOWASPService;
    private final ReportPMDService reportPMDService;
    private final ReportCheckStyleService reportCheckStyleService;
    private final ReportSpotBugsMapper reportSpotBugsMapper;
    private final ReportOWASPMapper reportOWASPMapper;
    private final ReportPMDMapper reportPMDMapper;
    private final ReportCheckStyleMapper reportCheckStyleMapper;
    private final ProjectMapper projectMapper;
    private final AgentServiceClient agentServiceClient;

//    @PostMapping("/create")
//    public int createReport(@RequestBody StartAnalysisDto startAnalysisDto) {
//        ProjectDto projectDtoNew = agentServiceClient.init(projectDto);
//        ReportPMDDto reportPmd = projectDtoNew.getReportPMDS().get(projectDtoNew.getReportPMDS().size() - 1);
//        ReportOWASPDto reportOWASPDto = projectDtoNew.getReportOWASPS().get(projectDtoNew.getReportOWASPS().size() - 1);
//        int idReportPMD = reportPMDService.create(reportPmd);
//        int idReportOWASP = reportOWASPService.create(reportOWASPDto);
//        return idReportOWASP;
//    }

    @GetMapping("/get-all-owasp")
    public List<ReportOWASPDto> getAllReportsOwasp() {
        return reportOWASPMapper.reportToListDto(reportOWASPService.findAll());
    }

    @GetMapping("/get-all-pmd")
    public List<ReportPMDDto> getAllReportsPmd() {
        return reportPMDMapper.reportToListDto(reportPMDService.findAll());
    }

    @GetMapping("/get-all-checkstyle")
    public List<ReportCheckStyleDto> getAllReportsCheckstyle() {
        return reportCheckStyleMapper.reportToListDto(reportCheckStyleService.findAll());
    }

    @GetMapping("/get-all-spotbugs")
    public List<ReportSpotBugsDto> getAllReportsSpotBugs() {
        return reportSpotBugsMapper.reportToListDto(reportSpotBugsService.findAll());
    }

    @GetMapping("/get-owasp/{id}")
    public ReportOWASPDto getReportOwasp(@PathVariable("id") int id) {
        return reportOWASPMapper.reportToDto(reportOWASPService.findById(id));
    }

    @GetMapping("/get-pmd/{id}")
    public ReportPMDDto getReportPMD(@PathVariable("id") int id) {
        return reportPMDMapper.reportToDto(reportPMDService.findById(id));
    }

    @GetMapping("/get-checkstyle/{id}")
    public ReportCheckStyleDto getReportCheckstyle(@PathVariable("id") int id) {
        return reportCheckStyleMapper.reportToDto(reportCheckStyleService.findById(id));
    }

    @GetMapping("/get-spotbugs/{id}")
    public ReportSpotBugsDto getReportSpotBugs(@PathVariable("id") int id) {
        return reportSpotBugsMapper.reportToDto(reportSpotBugsService.findById(id));
    }
}