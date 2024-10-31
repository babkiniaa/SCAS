package org.github.babkiniaa.scas.mapper;

import net.sourceforge.pmd.reporting.RuleViolation;
import org.github.babkiniaa.scas.dto.reportsDto.RuleViolationCustomDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportPMDMapper {

    RuleViolationCustomDto ruleViolationToRuleViolationCustom(RuleViolation violation);

    List<RuleViolationCustomDto> ruleViolationToRuleViolationCustomList(List<RuleViolation> reports);
}
