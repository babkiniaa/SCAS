package org.github.babkiniaa.scas.Mapper;

import org.github.babkiniaa.scas.dto.ReportPmdDto;
import org.github.babkiniaa.scas.entity.ReportPmd;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportPmdMapper {
    ReportPmd reportPmdToEntity(ReportPmdDto reportPmdDto);

    ReportPmdDto projectToDto(ReportPmd reportPmd);

    List<ReportPmdDto> projectToListDto(List<ReportPmd> reportPmds);
}
