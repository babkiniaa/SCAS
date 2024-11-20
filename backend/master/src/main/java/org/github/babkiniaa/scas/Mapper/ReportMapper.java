package org.github.babkiniaa.scas.Mapper;

import org.github.babkiniaa.scas.dto.ListReportDto;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    Report reportDtoToReport(ReportDto reportDto);

    ReportDto reportToReportDto(Report report);

    List<ListReportDto> reportsToReportsDto(List<Report> reports);

    Report updateReportFromDto(ReportDto reportDto, @MappingTarget Report report);
}
