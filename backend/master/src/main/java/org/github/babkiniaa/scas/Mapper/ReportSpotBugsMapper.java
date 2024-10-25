package org.github.babkiniaa.scas.Mapper;

import org.github.babkiniaa.scas.dto.ReportSpotBugsDto;
import org.github.babkiniaa.scas.entity.ReportSpotBugs;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportSpotBugsMapper {

    ReportSpotBugs reportToEntity(ReportSpotBugsDto reportDto);

    ReportSpotBugsDto reportToDto(ReportSpotBugs report);

    List<ReportSpotBugsDto> reportToListDto(List<ReportSpotBugs> reports);
}
