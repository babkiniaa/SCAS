package org.github.babkiniaa.scas.dto.Response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.dto.reportsDto.BugInstanceCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.DeadCodeDto.DieDeaDto;
import org.github.babkiniaa.scas.dto.reportsDto.DeadCodeDto.SummaryCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.DependencyCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.RuleViolationCustomDto;
import org.github.babkiniaa.scas.dto.reportsDto.ViolationCustomDto;
import org.github.babkiniaa.scas.entity.reportsEntity.DeadCodeEntity.SummaryCustom;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ReportAndDirDto {

    private String dir;

    private String hash;

    private String branch;

    private List<BugInstanceCustomDto> bugInstanceCustoms;

    private List<DependencyCustomDto> dependencyCustoms;

    private List<RuleViolationCustomDto> ruleViolationCustoms;

    private List<ViolationCustomDto> violationCustoms;

    private List<SummaryCustomDto> summaryCustoms;


}
