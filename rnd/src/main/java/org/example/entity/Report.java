package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Setter
@Getter
@Entity
@Table(name = "repots")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @Column(nullable = false)
//    private User user;

    private String reportDepencyChecker;

    private String reportCheckerStyle;

    private String reportPMD;

    private String reportBugs;

    public Report(String reportDepencyChecker, String reportCheckerStyle, String reportPMD, String reportBugs){
        this.reportDepencyChecker = reportDepencyChecker;
        this.reportCheckerStyle = reportCheckerStyle;
        this.reportPMD = reportPMD;
        this.reportBugs= reportBugs;
    }

    public Report(){

    }

}
