package org.github.babkiniaa.scas.dto.Response;

import lombok.Getter;
import lombok.Setter;
import org.github.babkiniaa.scas.entity.reportsEntity.BugInstanceCustom;
import org.github.babkiniaa.scas.entity.reportsEntity.DependencyCustom;
import org.github.babkiniaa.scas.entity.reportsEntity.RuleViolationCustom;
import org.github.babkiniaa.scas.entity.reportsEntity.ViolationCustom;

import java.util.List;

@Setter
@Getter
public class Report {

    private String hash;

    private List<BugInstanceCustom> bugInstanceCustoms;

    private List<DependencyCustom> dependencyCustoms;

    private List<RuleViolationCustom> ruleViolationCustoms;

    private List<ViolationCustom> violationCustoms;
}
