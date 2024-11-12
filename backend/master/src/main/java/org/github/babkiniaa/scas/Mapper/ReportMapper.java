package org.github.babkiniaa.scas.Mapper;


import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.entity.Report;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    Report reportDtoToReport(ReportDto reportDto);

    ReportDto reportToReportDto(Report report);
;
}
