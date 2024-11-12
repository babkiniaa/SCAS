package org.github.babkiniaa.scas.dto.oldReports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OWASPVulnerabilitiesDTO {

    private String name;

    private String description;

    private String notes;
}
