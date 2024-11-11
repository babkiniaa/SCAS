package org.github.babkiniaa.scas.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StartAnalyseDto {

    private Long idTask;

    private String url;

    private List<String> analysis;

}
