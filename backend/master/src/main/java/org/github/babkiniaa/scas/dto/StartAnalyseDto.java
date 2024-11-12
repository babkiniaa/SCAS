package org.github.babkiniaa.scas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class StartAnalyseDto {

    private Long projectId;

    private String url;

    private List<String> needReports;

}
