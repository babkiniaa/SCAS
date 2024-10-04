package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Builder
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

//    @Column(nullable = false)
//    private User user;

    private String nameProject;

    private Date date;

    private String reportDepencyChecker;

    private String reportCheckerStyle;

    private String reportPMD;

    private String reportBugs;

//    public Report(String nameProject, Date date, String reportDepencyChecker, String reportCheckerStyle, String reportPMD, String reportBugs){
//        this.nameProject = nameProject;
//        this.date = date;
//        this.reportDepencyChecker = reportDepencyChecker;
//        this.reportCheckerStyle = reportCheckerStyle;
//        this.reportPMD = reportPMD;
//        this.reportBugs= reportBugs;
//    }
//
//    public Report(){
//
//    }

}
