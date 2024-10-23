package org.github.babkiniaa.scas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.entity.Report;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private String name;

    private String description;

    private String url;

    private String[] listOfChecks;

    private boolean visibility = true;

    private Report report;

    private LocalDateTime createdDate;
}
