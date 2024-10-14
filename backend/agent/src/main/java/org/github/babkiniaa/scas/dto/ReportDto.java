package org.github.babkiniaa.scas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
