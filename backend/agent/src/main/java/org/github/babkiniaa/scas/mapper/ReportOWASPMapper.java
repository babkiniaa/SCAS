package org.github.babkiniaa.scas.mapper;

import org.github.babkiniaa.scas.dto.reportsDto.DependencyCustomDto;
import org.owasp.dependencycheck.dependency.Dependency;
import org.owasp.dependencycheck.dependency.Vulnerability;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
@Component
public class ReportOWASPMapper {

    public Dependency owaspCustomToOwasp(DependencyCustomDto dependencyCustomDto) {
        if ( dependencyCustomDto == null ) {
            return null;
        }

        Dependency dependency = new Dependency();

        dependency.setPackagePath( dependencyCustomDto.getPackagePath() );
        dependency.setFileName( dependencyCustomDto.getFileName() );
        dependency.setActualFilePath( dependencyCustomDto.getActualFilePath() );
        dependency.setFilePath( dependencyCustomDto.getFilePath() );
        dependency.setMd5sum( dependencyCustomDto.getMd5sum() );
        dependency.setSha1sum( dependencyCustomDto.getSha1sum() );
        dependency.setSha256sum( dependencyCustomDto.getSha256sum() );
        dependency.setDescription( dependencyCustomDto.getDescription() );
        dependency.setLicense( dependencyCustomDto.getLicense() );
        dependency.setName( dependencyCustomDto.getName() );
        dependency.setVersion( dependencyCustomDto.getVersion() );
        dependency.setEcosystem( dependencyCustomDto.getEcosystem() );
        if ( dependency.getProjectReferences() != null ) {
            Set<String> set = dependencyCustomDto.getProjectReferences();
            if ( set != null ) {
                dependency.getProjectReferences().addAll( set );
            }
        }
        if ( dependency.getAvailableVersions() != null ) {
            List<String> list = dependencyCustomDto.getAvailableVersions();
            if ( list != null ) {
                dependency.getAvailableVersions().addAll( list );
            }
        }

        return dependency;
    }

    public DependencyCustomDto owaspToOwaspCustom(Dependency dependency) {
        if ( dependency == null ) {
            return null;
        }

        DependencyCustomDto dependencyCustomDto = new DependencyCustomDto();

        Set<String> set = dependency.getProjectReferences();
        if ( set != null ) {
            dependencyCustomDto.setProjectReferences( new LinkedHashSet<String>( set ) );
        }
        List<String> list = dependency.getAvailableVersions();
        if ( list != null ) {
            dependencyCustomDto.setAvailableVersions( new ArrayList<String>( list ) );
        }
        dependencyCustomDto.setActualFilePath( dependency.getActualFilePath() );
        dependencyCustomDto.setFilePath( dependency.getFilePath() );
        dependencyCustomDto.setFileName( dependency.getFileName() );
        dependencyCustomDto.setPackagePath( dependency.getPackagePath() );
        dependencyCustomDto.setMd5sum( dependency.getMd5sum() );
        dependencyCustomDto.setSha1sum( dependency.getSha1sum() );
        dependencyCustomDto.setSha256sum( dependency.getSha256sum() );
        dependencyCustomDto.setDescription( dependency.getDescription() );
        dependencyCustomDto.setLicense( dependency.getLicense() );
        dependencyCustomDto.setVirtual( dependency.isVirtual() );
        dependencyCustomDto.setName( dependency.getName() );
        dependencyCustomDto.setVersion( dependency.getVersion() );
        dependencyCustomDto.setEcosystem( dependency.getEcosystem() );
        dependencyCustomDto.setOwaspVulnerabilities(OWASPVulnerabilitiesMapper
                .VulnerstoDTO(dependency
                        .getVulnerabilities()
                        .stream().toList()));

        return dependencyCustomDto;
    }

    public List<Dependency> owaspCustomToOwaspList(List<DependencyCustomDto> reports) {
        if ( reports == null ) {
            return null;
        }

        List<Dependency> list = new ArrayList<Dependency>( reports.size() );
        for ( DependencyCustomDto dependencyCustomDto : reports ) {
            list.add( owaspCustomToOwasp( dependencyCustomDto ) );
        }

        return list;
    }

    public List<DependencyCustomDto> owaspToOwaspCustomList(List<Dependency> reports) {
        if ( reports == null ) {
            return null;
        }

        List<DependencyCustomDto> list = new ArrayList<DependencyCustomDto>( reports.size() );
        for ( Dependency dependency : reports ) {
            list.add( owaspToOwaspCustom( dependency ) );
        }

        return list;
    }
}
