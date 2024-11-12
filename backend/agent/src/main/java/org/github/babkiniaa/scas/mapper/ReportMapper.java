package org.github.babkiniaa.scas.mapper;

import org.github.babkiniaa.scas.dto.Response.ReportDto;
import org.github.babkiniaa.scas.dto.Response.ReportAndDirDto;
import org.github.babkiniaa.scas.entity.Report;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    ReportDto ReportAndDirDtoToReportDto(ReportAndDirDto reportAndDir);

    ReportAndDirDto ReportDtoToReportAndDirDto(ReportDto report);

    Report reportDtoToReport(ReportDto reportDto);

    ReportDto reportToReportDto(Report report);

    Report reportAndDirToReport(ReportAndDirDto reportAndDirDto);
}
