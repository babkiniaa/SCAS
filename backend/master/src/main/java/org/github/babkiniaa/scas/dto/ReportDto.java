package org.github.babkiniaa.scas.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private String nameReport;

    private String reportDependencyChecker;

    private String reportCheckerStyle;

    private String reportPMD;

    private String reportBugs;

    public ReportDto (String nameReport){
        this.nameReport = nameReport;
    }
}
