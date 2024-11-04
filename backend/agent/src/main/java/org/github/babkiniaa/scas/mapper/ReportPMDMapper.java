package org.github.babkiniaa.scas.mapper;

import net.sourceforge.pmd.reporting.RuleViolation;
import org.github.babkiniaa.scas.dto.reportsDto.DependencyCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.RuleViolationCustomDto;
import org.mapstruct.Mapper;
import org.owasp.dependencycheck.dependency.Dependency;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportPMDMapper {
    RuleViolation ruleViolationCustomToRuleViolation(RuleViolationCustomDto ruleViolationCustomDto);

    RuleViolationCustomDto ruleViolationToRuleViolationCustom(RuleViolation violation);

    List<RuleViolation> ruleViolationCustomToRuleViolationList(List<RuleViolationCustomDto> reports);

    List<RuleViolationCustomDto> ruleViolationToRuleViolationCustomList(List<RuleViolation> reports);
}
