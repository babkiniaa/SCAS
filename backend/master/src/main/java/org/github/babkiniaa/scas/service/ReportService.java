package org.github.babkiniaa.scas.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.Mapper.ReportMapper;
import org.github.babkiniaa.scas.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    private final ReportMapper reportMapper;

    public Report create(ReportDto reportDto) {
        return reportRepository.save(reportMapper.reportToEntity(reportDto));
    }

    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    public Report update(ReportDto reportDto) {
        Report report = reportMapper.reportToEntity(reportDto);
        return reportRepository.save(report);
    }

    public void delete(Integer id) {
        Optional<Report> reportOpt = reportRepository.findById(id);

        if (reportOpt.isPresent()) {
            reportRepository.delete(reportOpt.get());
        } else {
            throw new EntityNotFoundException("Report with ID " + id + " not found.");
        }
    }

    public Optional<Report> findById(Integer id) {
        return reportRepository.findById(id);
    }

    public Report updateOWASP(Integer id, String rep) {
        Report report = new Report();
        if (!findById(id).isEmpty()) {
            report = findById(id).get();
            report.setReportDependencyChecker(rep);
            save(report);
        }
        return report;
    }

    public Report updateSpotbugs(Integer id, String rep) {
        Report report = new Report();
        if (!findById(id).isEmpty()) {
            report = findById(id).get();
            report.setReportBugs(rep);
            save(report);
        }
        return report;
    }

    public Report updatePmd(Integer id, String rep) {
        Report report = new Report();
        if (!findById(id).isEmpty()) {
            report = findById(id).get();
            report.setReportPMD(rep);
            save(report);
        }
        return report;
    }

    public Report updateCheckStyle(Integer id, String rep) {
        Report report = new Report();
        if (!findById(id).isEmpty()) {
            report = findById(id).get();
            report.setReportCheckerStyle(rep);
            save(report);
        }
        return report;
    }

    public Report save(Report report){
        return reportRepository.save(report);
    }

    public void create(Report reportToEntity) {
    }
}
