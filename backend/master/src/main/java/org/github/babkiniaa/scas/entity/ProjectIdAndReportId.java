package org.github.babkiniaa.scas.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ProjectIdAndReportId {

    private long projectId;

    private long reportId;
}
