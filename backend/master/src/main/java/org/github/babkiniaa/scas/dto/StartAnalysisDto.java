package org.github.babkiniaa.scas.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StartAnalysisDto {
    private List<String> needReports;
    private String url;
    private int id;
}
