package org.example.Mapper;

import org.example.dto.ReportDto;
import org.example.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ReportMapper {

    ReportDto ReportToReportDto(Report report);
}
