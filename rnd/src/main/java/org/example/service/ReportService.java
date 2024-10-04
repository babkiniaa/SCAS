package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dto.ReportDto;
import org.example.entity.Report;
import org.example.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public Report create(ReportDto reportDto) {
        return reportRepository.save(Report.builder()
                .nameProject(reportDto.getNameProject())
                .reportDepencyChecker(reportDto.getReportDepencyChecker())
                .reportCheckerStyle(reportDto.getReportCheckerStyle())
                .reportPMD(reportDto.getReportPMD())
                .reportBugs(reportDto.getReportBugs())
                .build());
    }

    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public Report update(Report report) {
        return reportRepository.save(report);
    }

    public Optional<Report> findById(Integer id) {
        return reportRepository.findById(id);
    }
}
