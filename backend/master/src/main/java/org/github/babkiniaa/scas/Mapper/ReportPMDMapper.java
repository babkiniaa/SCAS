package org.github.babkiniaa.scas.Mapper;

import org.github.babkiniaa.scas.dto.ReportPMDDto;
import org.github.babkiniaa.scas.entity.ReportPMD;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportPMDMapper {

    ReportPMD reportToEntity(ReportPMDDto reportDto);

    ReportPMDDto reportToDto(ReportPMD report);

    List<ReportPMDDto> reportToListDto(List<ReportPMD> reports);
}
