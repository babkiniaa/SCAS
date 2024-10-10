package org.github.babkiniaa.scas.Mapper;

import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.entity.Report;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ReportMapper {

    Report reportToEntity(ReportDto reportDto);
    ReportDto reportToDto(Report report);
    List<ReportDto> reportToListDto(List<Report> reports);
}
