package org.github.babkiniaa.scas.service;

import lombok.AllArgsConstructor;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.mapper.ReportMapper;
import org.github.babkiniaa.scas.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReportService {
    @Autowired
    private final ReportRepository reportRepository;
    @Autowired
    private final ReportMapper reportMapper;

    public Report create(ReportDto reportDto) {
        return reportRepository.save(reportMapper.reportToEntity(reportDto));
    }

    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public Report update(ReportDto reportDto) {
        return reportRepository.save(reportMapper.reportToEntity(reportDto));
    }

    public Report updateOWASP(Integer id, String rep) {
        ReportDto reportDto = new ReportDto();
        if (!findById(id).isEmpty()) {
            Report report = findById(id).get();
            reportDto.setNameReport(report.getNameReports());
            reportDto.setReportDependencyChecker(rep);
            reportDto.setReportCheckerStyle(report.getReportCheckerStyle());
            reportDto.setReportPMD(report.getReportPMD());
            reportDto.setReportBugs(report.getReportBugs());
        }
        return update(reportDto);
    }

    public Optional<Report> findById(Integer id) {
        return reportRepository.findById(id);
    }

    public void create(Report reportToEntity) {
    }
}
