package org.github.babkiniaa.scas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.dto.reportsDto.DependencyCustomDto;
import org.owasp.dependencycheck.dependency.Dependency;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportOWASPDto {

    private String hash;

    private List<DependencyCustomDto> reportList;

}
