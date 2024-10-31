package org.github.babkiniaa.scas.mapper;

import com.puppycrawl.tools.checkstyle.api.Violation;
import org.github.babkiniaa.scas.dto.reportsDto.ViolationCustomDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportCheckStyleMapper {

    Violation checkstyleCustomTocCheckStyle(ViolationCustomDto violationCustomDto);

    ViolationCustomDto checkStyleToCheckstyleCustom(Violation violation);

    List<Violation> checkStyleCustomToCheckStyleList(List<ViolationCustomDto> reports);

    List<ViolationCustomDto> checkStyleToCheckStyleCustomList(List<Violation> reports);
}
