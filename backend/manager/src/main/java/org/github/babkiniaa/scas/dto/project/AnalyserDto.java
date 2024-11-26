package org.github.babkiniaa.scas.dto.project;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * DTO посылаемое на мастер для проведения анализа проекта
 **/
@Setter
@Getter
public class AnalyserDto {
    private Long idProject;

    private String branch;

    private String commit;

    private List<String> needReports;
}
