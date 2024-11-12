package org.github.babkiniaa.scas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.dto.oldReports.ReportCheckStyleDto;
import org.github.babkiniaa.scas.dto.oldReports.ReportOWASPDto;
import org.github.babkiniaa.scas.dto.oldReports.ReportPMDDto;
import org.github.babkiniaa.scas.dto.oldReports.ReportSpotBugsDto;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private String name;

    private String description;

    private String url;

    private boolean visibility = true;

    private LocalDateTime createdDate;

    private List<String> needReports;

    private List<ReportCheckStyleDto> reportCheckStyles;

    private List<ReportOWASPDto> reportOWASPS;

    private List<ReportPMDDto> reportPMDS;

    private List<ReportSpotBugsDto> reportSpotBugs;
    
}
