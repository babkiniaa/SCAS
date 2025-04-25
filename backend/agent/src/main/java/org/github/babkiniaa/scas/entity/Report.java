package org.github.babkiniaa.scas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.entity.reportsEntity.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "reports")
@Table
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String hash;

    private String branch;

    @OneToMany(cascade = CascadeType.ALL)
    private List<AttentionsCustom> attentionsCustom;

    @OneToMany(cascade = CascadeType.ALL)
    private List<BugInstanceCustom> bugInstanceCustoms;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DependencyCustom> dependencyCustoms;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RuleViolationCustom> ruleViolationCustoms;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ViolationCustom> violationCustoms;
}
