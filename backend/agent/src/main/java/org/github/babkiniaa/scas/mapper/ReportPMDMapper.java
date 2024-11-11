package org.github.babkiniaa.scas.mapper;

import net.sourceforge.pmd.reporting.RuleViolation;
import org.github.babkiniaa.scas.dto.reportsDto.RuleViolationCustomDto;
import org.github.babkiniaa.scas.entity.reportsEntity.RuleViolationCustom;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ReportPMDMapper {

    RuleViolationCustomDto PMDtoDTO(RuleViolation ruleViolation);

    RuleViolationCustom PMDtoCustom(RuleViolation ruleViolation);

    List<RuleViolationCustomDto> PMDtoDTO(List<RuleViolation> ruleViolations);
}
