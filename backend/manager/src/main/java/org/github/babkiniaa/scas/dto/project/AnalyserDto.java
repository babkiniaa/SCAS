package org.github.babkiniaa.scas.dto.project;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AnalyserDto {

    private Long idProject;

    private List<String> needReports;
}
