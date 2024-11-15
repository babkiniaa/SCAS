package org.github.babkiniaa.scas.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StartAnalyseDto {

    private long taskId;

    private long projectId;

    private String url;

    private List<String> needReports;

}
