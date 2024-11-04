package org.github.babkiniaa.scas.mapper;

import net.sourceforge.pmd.reporting.RuleViolation;
import org.github.babkiniaa.scas.dto.reportsDto.RuleViolationCustomDto;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReportPMDMapper {
    public RuleViolationCustomDto PMDtoDTO(RuleViolation ruleViolation) {
        RuleViolationCustomDto ruleViolationCustomDto = new RuleViolationCustomDto();

        ruleViolationCustomDto.setName(ruleViolation.getRule().getName());
        ruleViolationCustomDto.setDescription(ruleViolation.getDescription());
        ruleViolationCustomDto.setMessage(ruleViolation.getRule().getMessage());
        ruleViolationCustomDto.setPriority(ruleViolation.getRule().getPriority().getName());

        return ruleViolationCustomDto;
    }

    public List<RuleViolationCustomDto> PMDtoDTO(List<RuleViolation> ruleViolations) {

        List<RuleViolationCustomDto> pmdDTOs = new ArrayList<>();

        for (RuleViolation rv : ruleViolations) {
            pmdDTOs.add(PMDtoDTO(rv));
        }

        return pmdDTOs;
    }
}
