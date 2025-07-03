package org.github.babkiniaa.scas.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class AnalyserDto {

    private long idProject;
    private List<String> needReports;
    private String branch;
    private String commit;
    private String url;
}
