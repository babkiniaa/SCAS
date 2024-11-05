package org.github.babkiniaa.scas.Mapper;

import org.github.babkiniaa.scas.dto.ReportOWASPDto;
import org.github.babkiniaa.scas.entity.ReportOWASP;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportOWASPMapper {

    ReportOWASP reportToEntity(ReportOWASPDto reportDto);

    ReportOWASPDto reportToDto(ReportOWASP report);

    List<ReportOWASPDto> reportToListDto(List<ReportOWASP> reports);
}
