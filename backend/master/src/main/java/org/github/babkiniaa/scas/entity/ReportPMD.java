package org.github.babkiniaa.scas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.entity.reportsEntity.RuleViolation;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Table
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "report_PMD")
public class ReportPMD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private String hash;

    @Column(name = "created_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany
    private List<RuleViolation> reportList;

}
