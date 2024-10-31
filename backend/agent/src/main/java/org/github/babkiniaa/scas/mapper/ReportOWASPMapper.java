package org.github.babkiniaa.scas.mapper;

import org.github.babkiniaa.scas.dto.reportsDto.DependencyCustomDto;
import org.mapstruct.Mapper;
import org.owasp.dependencycheck.dependency.Dependency;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportOWASPMapper {

    Dependency owaspCustomToOwasp(DependencyCustomDto dependencyCustomDto);

    DependencyCustomDto owaspToOwaspCustom(Dependency dependency);

    List<Dependency> owaspCustomToOwaspList(List<DependencyCustomDto> reports);

    List<DependencyCustomDto> owaspToOwaspCustomList(List<Dependency> reports);
}
