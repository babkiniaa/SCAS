package org.github.babkiniaa.scas.dto;

import lombok.*;
import net.sourceforge.pmd.reporting.RuleViolation;
import org.github.babkiniaa.scas.entity.ReportPmd;

import java.util.List;

/**
 * DTO for {@link ReportPmd}
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportPmdDto {
    ProjectDto project;
    String commitHash;
    List<RuleViolation> ruleViolationList;
}