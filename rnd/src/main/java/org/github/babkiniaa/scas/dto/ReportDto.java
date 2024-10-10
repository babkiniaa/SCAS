package org.github.babkiniaa.scas.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {
    private String nameProject;

    private String reportDepencyChecker;

    private String reportCheckerStyle;

    private String reportPMD;

    private String reportBugs;
}
