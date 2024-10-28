package org.github.babkiniaa.scas.entity.reportsEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.owasp.dependencycheck.dependency.IncludedByReference;
import org.owasp.dependencycheck.dependency.Vulnerability;
import org.owasp.dependencycheck.dependency.naming.Identifier;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "dependency")
@Table
public class Dependency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    private Set<Identifier> softwareIdentifiers;
//
//    private Set<Identifier> vulnerableSoftwareIdentifiers;
//
//    private Set<Identifier> suppressedIdentifiers;
//
//    private Set<Vulnerability> suppressedVulnerabilities;
//
//    private Set<Vulnerability> vulnerabilities;

//    private SortedSet<org.owasp.dependencycheck.dependency.Dependency> relatedDependencies;

//    private Set<IncludedByReference> includedBy;
//
    private Set<String> projectReferences;

    private List<String> availableVersions;

    private String actualFilePath;

    private String filePath;

    private String fileName;

    private String packagePath;

    private String md5sum;

    private String sha1sum;

    private String sha256sum;

    private String displayName;

    private String description;

    private String license;

    private boolean isVirtual;

    private String name;

    private String version;

    private String ecosystem;

}
