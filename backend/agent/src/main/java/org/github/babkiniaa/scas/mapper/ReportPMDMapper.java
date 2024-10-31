package org.github.babkiniaa.scas.mapper;

import net.sourceforge.pmd.reporting.RuleViolation;
import org.github.babkiniaa.scas.dto.reportsDto.RuleViolationCustomDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportPMDMapper {

    RuleViolation ruleViolationCustomToRuleViolation(RuleViolationCustomDto violationCustomDto);

    RuleViolationCustomDto ruleViolationToRuleViolationCustom(RuleViolation violation);

    List<RuleViolation> ruleViolationCustomToRuleViolationList(List<RuleViolationCustomDto> reports);

    List<RuleViolationCustomDto> ruleViolationToRuleViolationCustomList(List<RuleViolation> reports);
}
