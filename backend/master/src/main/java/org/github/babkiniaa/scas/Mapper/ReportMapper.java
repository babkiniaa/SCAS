package org.github.babkiniaa.scas.Mapper;

import org.github.babkiniaa.scas.dto.ListReportDto;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.dto.Response.ReportAndIdProjectDto;
import org.github.babkiniaa.scas.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE , componentModel = "spring")
public interface ReportMapper {

    Report reportReportAndIdProjectDto(ReportAndIdProjectDto reportAndIdProjectDto);

    Report reportDtoToReport(ReportDto reportDto);

    ReportDto reportToReportDto(Report report);

    List<ListReportDto> reportsToReportsDto(List<Report> reports);

    void updateReportFromDto(ReportAndIdProjectDto reportDto, @MappingTarget Report report);
}
