package org.github.babkiniaa.scas.dto.ProjectAndId;

import lombok.Getter;
import lombok.Setter;
import org.github.babkiniaa.scas.dto.ReportCheckStyleDto;
import org.github.babkiniaa.scas.dto.ReportOWASPDto;
import org.github.babkiniaa.scas.dto.ReportPMDDto;
import org.github.babkiniaa.scas.dto.ReportSpotBugsDto;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ProjectAndUserIdDto {

    private int userId;

    private String name;

    private String description;

    private String url;

    private boolean visibility = true;

    private LocalDateTime createdDate;

    private List<ReportCheckStyleDto> reportCheckStyles;

    private List<ReportOWASPDto> reportOWASPS;

    private List<ReportPMDDto> reportPMDS;

    private List<ReportSpotBugsDto> reportSpotBugs;
}
