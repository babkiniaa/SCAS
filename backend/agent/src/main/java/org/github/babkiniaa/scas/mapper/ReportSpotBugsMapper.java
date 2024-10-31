package org.github.babkiniaa.scas.mapper;

import edu.umd.cs.findbugs.BugInstance;
import org.github.babkiniaa.scas.dto.reportsDto.BugInstanceCustomDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportSpotBugsMapper {

    BugInstanceCustomDto bugInstanceToBugInstanceCustom(BugInstance violation);

    List<BugInstanceCustomDto> bugInstanceToBugInstanceCustomList(List<BugInstance> reports);
}
