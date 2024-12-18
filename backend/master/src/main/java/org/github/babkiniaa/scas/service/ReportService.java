package org.github.babkiniaa.scas.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.Mapper.ReportMapper;
import org.github.babkiniaa.scas.dto.ListReportDto;
import org.github.babkiniaa.scas.dto.ReportDto;
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

    public long countBugsLastReport(long id) {
        Report report = reportRepository.findReportById(id);
        return report.getBugInstanceCustoms().size() + report.getDependencyCustoms().size() +
                report.getRuleViolationCustoms().size() + report.getViolationCustoms().size();
    }
}
