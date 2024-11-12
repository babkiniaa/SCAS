package org.github.babkiniaa.scas.dto.typeForMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.github.babkiniaa.scas.dto.Response.ReportAndDirDto;

import java.util.function.Function;

@AllArgsConstructor
@Getter
@Setter
public class MethodAndTypeAnalysis {

    private Function<ReportAndDirDto, ?> function;

    private String type;

}
