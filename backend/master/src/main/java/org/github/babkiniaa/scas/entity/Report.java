package org.github.babkiniaa.scas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Table
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nameReports;

    private Date date;
    @Column(columnDefinition = "TEXT")
    private String reportDependencyChecker;
    @Column(columnDefinition = "TEXT")
    private String reportCheckerStyle;
    @Column(columnDefinition = "TEXT")
    private String reportPMD;
    @Column(columnDefinition = "TEXT")
    private String reportBugs;


}
