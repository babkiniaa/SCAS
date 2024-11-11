package org.github.babkiniaa.scas.dto.typeForMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.github.babkiniaa.scas.dto.ProjectDto;
import org.github.babkiniaa.scas.dto.Request.RegisterTaskDto;
import org.github.babkiniaa.scas.dto.Response.ReportAndDir;

import java.util.function.Function;

@AllArgsConstructor
@Getter
@Setter
public class MethodAndTypeAnalysis {

    private Function<ReportAndDir, ?> function;

    private String type;

}
