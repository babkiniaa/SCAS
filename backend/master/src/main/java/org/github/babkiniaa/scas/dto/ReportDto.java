package org.github.babkiniaa.scas.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.dto.reportsDto.BugInstanceCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.DependencyCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.RuleViolationCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.ViolationCustomDto;
import org.github.babkiniaa.scas.entity.reportsEntity.BugInstanceCustom;
import org.github.babkiniaa.scas.entity.reportsEntity.DependencyCustom;
import org.github.babkiniaa.scas.entity.reportsEntity.RuleViolationCustom;
import org.github.babkiniaa.scas.entity.reportsEntity.ViolationCustom;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class ReportDto {

    private String hash;

    private List<BugInstanceCustomDto> bugInstanceCustoms;

    private List<DependencyCustomDto> dependencyCustoms;

    private List<RuleViolationCustomDto> ruleViolationCustoms;

    private List<ViolationCustomDto> violationCustoms;
}
