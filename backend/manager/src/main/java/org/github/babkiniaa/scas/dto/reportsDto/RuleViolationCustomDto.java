package org.github.babkiniaa.scas.dto.reportsDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.dto.OWASPVulnerabilitiesDTO;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RuleViolationCustomDto {

    private String name;

    private String message;

    private String priority;

    private String description;

    private int beginLine;

    private int endLine;

    private int beginColumn;

    private int endColumn;

    private String fileName;

    private List<OWASPVulnerabilitiesDTO> owaspVulnerabilities;
}
