package org.github.babkiniaa.scas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.entity.reportsEntity.BugInstanceCustom;
import org.github.babkiniaa.scas.entity.reportsEntity.DeadCodeEntity.SummaryCustom;
import org.github.babkiniaa.scas.entity.reportsEntity.DependencyCustom;
import org.github.babkiniaa.scas.entity.reportsEntity.RuleViolationCustom;
import org.github.babkiniaa.scas.entity.reportsEntity.ViolationCustom;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "reports")
@Table
@EntityListeners(AuditingEntityListener.class)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String hash;

    private String branch;

    private long projectId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<BugInstanceCustom> bugInstanceCustoms;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DependencyCustom> dependencyCustoms;

    @OneToMany(cascade = CascadeType.ALL)
    private List<RuleViolationCustom> ruleViolationCustoms;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ViolationCustom> violationCustoms;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SummaryCustom> summaryCustoms;

    @Column(name = "created_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createdDate;

    private List<String> analyzers;
}
