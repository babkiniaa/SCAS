package org.github.babkiniaa.scas.service;

import lombok.AllArgsConstructor;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.mapper.ReportMapper;
import org.github.babkiniaa.scas.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Component
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
        return reportRepository.save(reportMapper.reportToEntity(reportDto));
    }

    public Optional<Report> findById(Integer id) {
        return reportRepository.findById(id);
    }

    public Report updateOWASP(Integer id, String rep) {
        ReportDto reportDto = new ReportDto();
        if (!findById(id).isEmpty()) {
            Report report = findById(id).get();
            reportDto = reportMapper.reportToDto(report);
            reportDto.setReportDependencyChecker(rep);
        }
        return update(reportDto);
    }

    public Report updateSpotbugs(Integer id, String rep) {
        ReportDto reportDto = new ReportDto();
        if (!findById(id).isEmpty()) {
            Report report = findById(id).get();
            reportDto = reportMapper.reportToDto(report);
            reportDto.setReportBugs(rep);
        }
        return update(reportDto);
    }

    public Report updatePmd(Integer id, String rep) {
        ReportDto reportDto = new ReportDto();
        if (!findById(id).isEmpty()) {
            Report report = findById(id).get();
            reportDto = reportMapper.reportToDto(report);
            reportDto.setReportPMD(rep);
        }
        return update(reportDto);
    }

    public Report updateCheckStyle(Integer id, String rep) {
        ReportDto reportDto = new ReportDto();
        if (!findById(id).isEmpty()) {
            Report report = findById(id).get();
            reportDto = reportMapper.reportToDto(report);
            reportDto.setReportCheckerStyle(rep);
        }
        return update(reportDto);
    }

    public void create(Report reportToEntity) {
    }
}
