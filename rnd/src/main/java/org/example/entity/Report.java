package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Builder
@Setter
@Getter
@Table
@Component
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
