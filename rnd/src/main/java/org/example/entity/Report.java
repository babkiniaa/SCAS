package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String nameProject;

    private Date date;

    private String reportDepencyChecker;

    private String reportCheckerStyle;

    private String reportPMD;

    private String reportBugs;


}
