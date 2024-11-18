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
import java.util.List;

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
        Report reportEntity = reportMapper.reportDtoToReport(report);
        reportEntity.setProjectId(projectId);

        return reportRepository.save(reportEntity);
    }

    public ReportDto findById(long reportId){
        return reportMapper.reportToReportDto(reportRepository.findById(reportId).get());
    }

    public List<ListReportDto> findAllByProjectId(long projectId){
        return reportMapper.reportsToReportsDto(reportRepository.findAllByProjectId(projectId));
    }

}
