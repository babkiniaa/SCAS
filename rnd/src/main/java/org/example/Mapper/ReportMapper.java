package org.example.Mapper;

import org.example.dto.ReportDto;
import org.example.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ReportMapper {

    Report reportToEntity(ReportDto reportDto);
    ReportDto reportToDto(Report report);
    List<ReportDto> reportToListDto(List<Report> reports);
}
