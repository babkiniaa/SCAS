package org.example.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ReportDto {
    private String nameProject;

    private String reportDepencyChecker;

    private String reportCheckerStyle;

    private String reportPMD;

    private String reportBugs;
}
