package org.github.babkiniaa.scas.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnalyserDto {

    private long idProject;

    private List<String> needReports;

    private String branch;

    private String commit;

    private String url;
}
