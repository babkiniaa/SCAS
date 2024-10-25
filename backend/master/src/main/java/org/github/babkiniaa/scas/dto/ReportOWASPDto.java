package org.github.babkiniaa.scas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.owasp.dependencycheck.dependency.Dependency;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportOWASPDto {

    private int id;

    private String hash;

    private List<Dependency> reportList;

}
