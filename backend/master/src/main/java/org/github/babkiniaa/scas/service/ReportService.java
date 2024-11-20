package org.github.babkiniaa.scas.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.Mapper.Custom.ReportOWASPMapper;
import org.github.babkiniaa.scas.Mapper.Custom.ReportPMDMapper;
import org.github.babkiniaa.scas.Mapper.ReportMapper;
import org.github.babkiniaa.scas.dto.ListReportDto;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Setter
@Getter
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final ReportPMDMapper reportPMDMapper;
    private final ReportOWASPMapper reportOWASPMapper;

    public Report save(Long projectId, ReportDto report) {
        Optional<Report> previous = reportRepository.findByHash(report.getHash());
        Report reportEntity = new Report();
        if (previous.isEmpty()) {
            reportEntity = reportMapper.reportDtoToReport(report);
            reportEntity.setProjectId(projectId);
        } else {
            reportEntity = previous.get();
            List<String> analyzers = report.getAnalyzers();
            analyzers.addAll(reportEntity.getAnalyzers());
            report.setAnalyzers(analyzers);
            reportMapper.updateReportFromDto(report, reportEntity);
            reportEntity.setCreatedDate(LocalDateTime.now());
        }
        return reportRepository.save(reportEntity);
    }

    public ReportDto findById(long reportId){
        return reportMapper.reportToReportDto(reportRepository.findById(reportId).get());
    }

    public List<ListReportDto> findAllByProjectId(long projectId){
        return reportMapper.reportsToReportsDto(reportRepository.findAllByProjectId(projectId));
    }

    public List<String> findAnalyzes(String hash) {
        Optional<Report> report = reportRepository.findByHash(hash);
        return report.map(Report::getAnalyzers).orElse(new ArrayList<>());
    }
}
