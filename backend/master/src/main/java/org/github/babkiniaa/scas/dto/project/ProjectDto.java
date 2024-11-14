package org.github.babkiniaa.scas.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.entity.Report;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private int id;

    private String name;

    private String description;

    private String url;

    private boolean visibility = true;

    private LocalDateTime createdDate;

    private List<String> needReports;

    private List<Report> reports;

}
