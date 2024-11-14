package org.github.babkiniaa.scas.service;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.dto.Response.ReportAndDirDto;
import org.github.babkiniaa.scas.dto.Response.ReportDto;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.mapper.ReportMapper;
import org.github.babkiniaa.scas.repository.ReportRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * The type Report service.
 */
@Getter
@Setter
@RequiredArgsConstructor
@Service
public class ReportService {

    private final ReportRepository repository;
    private final ReportMapper reportMapper;

    /**
     * Save report.
     *
     * @param reportDto the report dto
     * @return the report
     */
    public Report save(ReportDto reportDto){
        return repository.save(reportMapper.reportDtoToReport(reportDto));
    }

    /**
     * Save report.
     *
     * @param reportAndDirDto the report and dir dto
     * @return the report
     */
    public Report save(ReportAndDirDto reportAndDirDto){
        return repository.save(reportMapper.reportAndDirToReport(reportAndDirDto));
    }
}
