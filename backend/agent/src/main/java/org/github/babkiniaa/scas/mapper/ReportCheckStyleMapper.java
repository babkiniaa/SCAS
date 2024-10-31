package org.github.babkiniaa.scas.mapper;

import com.puppycrawl.tools.checkstyle.api.Violation;
import org.github.babkiniaa.scas.dto.reportsDto.ViolationCustomDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportCheckStyleMapper {

    ViolationCustomDto checkStyleToCheckstyleCustom(Violation violation);

    List<ViolationCustomDto> checkStyleToCheckStyleCustomList(List<Violation> reports);
}
