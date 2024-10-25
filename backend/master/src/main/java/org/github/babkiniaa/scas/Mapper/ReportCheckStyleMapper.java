package org.github.babkiniaa.scas.Mapper;

import org.github.babkiniaa.scas.dto.ReportCheckStyleDto;
import org.github.babkiniaa.scas.dto.ReportDto;
import org.github.babkiniaa.scas.entity.Report;
import org.github.babkiniaa.scas.entity.ReportCheckStyle;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportCheckStyleMapper {

    ReportCheckStyle reportToEntity(ReportCheckStyleDto reportDto);

    ReportCheckStyleDto reportToDto(ReportCheckStyle report);

    List<ReportCheckStyleDto> reportToListDto(List<ReportCheckStyle> reports);
}
