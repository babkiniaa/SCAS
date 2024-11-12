package org.github.babkiniaa.scas.Mapper.Custom;


import org.github.babkiniaa.scas.dto.OWASPVulnerabilitiesDTO;
import org.owasp.dependencycheck.dependency.Vulnerability;

import java.util.ArrayList;
import java.util.List;

public class OWASPVulnerabilitiesMapper {

    public static OWASPVulnerabilitiesDTO VulnertoDTO(Vulnerability vulnerability) {
        OWASPVulnerabilitiesDTO owaspVulnerabilitiesDTO = new OWASPVulnerabilitiesDTO();

        owaspVulnerabilitiesDTO.setName(vulnerability.getName());
        owaspVulnerabilitiesDTO.setDescription(vulnerability.getDescription());
        owaspVulnerabilitiesDTO.setNotes(vulnerability.getNotes());


        return owaspVulnerabilitiesDTO;
    }

    public static List<OWASPVulnerabilitiesDTO> VulnerstoDTO(List<Vulnerability> vulnerabilities) {

        List<OWASPVulnerabilitiesDTO> vulnDTOs = new ArrayList<>();

        for (Vulnerability v : vulnerabilities) {
            vulnDTOs.add(VulnertoDTO(v));
        }

        return vulnDTOs;
    }

}
