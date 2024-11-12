package org.github.babkiniaa.scas.dto.typeForMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.github.babkiniaa.scas.dto.project.ProjectDto;

import java.util.function.Function;

@AllArgsConstructor
@Getter
@Setter
public class MethodAndTypeAnalysis {

    private Function<ProjectDto, ?> function;

    private String type;

}
