package org.github.babkiniaa.scas.Mapper;

import org.github.babkiniaa.scas.dto.OWASPVulnerabilitiesDTO;
import org.github.babkiniaa.scas.dto.ReportOWASPDto;
import org.github.babkiniaa.scas.entity.OWASPVulnerabilities;
import org.github.babkiniaa.scas.entity.ReportOWASP;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OWASPVulnerabilitiesMapper {

    OWASPVulnerabilities vulnerToEntity(OWASPVulnerabilitiesDTO vulnerabilitiesDTO);

    OWASPVulnerabilitiesDTO vulnerToDto(OWASPVulnerabilities vulnerabilities);

    List<OWASPVulnerabilitiesDTO> vulnerToListDto(List<OWASPVulnerabilities> vulnerabilities);
}
