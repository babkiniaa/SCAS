package org.github.babkiniaa.scas.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.dto.reportsDto.BugInstanceCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.DependencyCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.RuleViolationCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.ViolationCustomDto;
import org.github.babkiniaa.scas.entity.reportsEntity.DeadCodeEntity.SummaryCustom;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class ReportDto {

    private String hash;

    private String branch;

    private List<BugInstanceCustomDto> bugInstanceCustoms;

    private List<DependencyCustomDto> dependencyCustoms;

    private List<RuleViolationCustomDto> ruleViolationCustoms;

    private List<ViolationCustomDto> violationCustoms;

    private List<SummaryCustom> summaryCustoms;


    private List<String> analyzers;
}
