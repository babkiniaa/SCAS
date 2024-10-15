package org.github.babkiniaa.scas.Mapper;

import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.entity.Report;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    Report reportToEntity(ReportDto reportDto);

    ReportDto reportToDto(Report report);

    List<ReportDto> reportToListDto(List<Report> reports);
}
