package org.github.babkiniaa.scas.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.Mapper.Custom.ReportOWASPMapper;
import org.github.babkiniaa.scas.Mapper.Custom.ReportPMDMapper;
import org.github.babkiniaa.scas.Mapper.ReportMapper;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.repository.ReportRepository;
import org.springframework.stereotype.Service;

@Service
@Setter
@Getter
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final ReportPMDMapper reportPMDMapper;
    private final ReportOWASPMapper reportOWASPMapper;

    public Report save(ReportDto report) {
        return reportRepository.save(reportMapper.reportDtoToReport(report));
    }

    public ReportDto findById(long reportId){
        return reportMapper.reportToReportDto(reportRepository.findById(reportId).get());
    }

}
