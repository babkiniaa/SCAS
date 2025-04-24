package org.github.babkiniaa.scas.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.dto.reportsDto.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportAndIdProjectDto {

    private long projectId;

    private String hash;

    private String branch;

    private List<AttentionsCustomDto> attentionsCustomDtos;

    private List<BugInstanceCustomDto> bugInstanceCustoms;

    private List<DependencyCustomDto> dependencyCustoms;

    private List<RuleViolationCustomDto> ruleViolationCustoms;

    private List<ViolationCustomDto> violationCustoms;

    private List<String> analyzers;
}

