package org.github.babkiniaa.scas.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.Mapper.ReportMapper;
import org.github.babkiniaa.scas.dto.AnalyserDto;
import org.github.babkiniaa.scas.dto.ListReportDto;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.dto.project.ProjectDto;
import org.github.babkiniaa.scas.entity.Project;
import org.github.babkiniaa.scas.entity.ProjectIdAndReportId;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Setter
@Getter
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final ProjectService projectService;
    private final MetricsService metricsService;

    public AnalyserDto startOffline(ProjectIdAndReportId projectIdAndReportId){
        AnalyserDto analyserDto = new AnalyserDto();
        ReportDto reportDto = findById(projectIdAndReportId.getReportId());
        ProjectDto projectDto = projectService.findById(projectIdAndReportId.getProjectId());

        analyserDto.setBranch(reportDto.getBranch());
        analyserDto.setUrl(projectDto.getUrl());
        analyserDto.setCommit(reportDto.getHash());
        analyserDto.setNeedReports(reportDto.getAnalyzers());
        analyserDto.setIdProject(projectIdAndReportId.getProjectId());

        metricsService.userTask(projectService.findByIdProject(analyserDto.getIdProject()).getUserId(), 1);

        return analyserDto;
    }

    public ReportDto findById(long reportId) {
        return reportMapper.reportToReportDto(reportRepository.findById(reportId).get());
    }

    public List<ListReportDto> findAllByProjectId(long projectId) {
        return reportMapper.reportsToReportsDto(reportRepository.findAllByProjectId(projectId));
    }

    public List<String> findAnalyzes(String hash, long projectId) {
        Optional<Report> report = reportRepository.findByHashAndProjectId(hash, projectId);
        return report.map(Report::getAnalyzers).orElse(new ArrayList<>());
    }
}
