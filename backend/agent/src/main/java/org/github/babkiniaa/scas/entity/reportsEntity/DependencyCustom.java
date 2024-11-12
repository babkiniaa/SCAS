package org.github.babkiniaa.scas.entity.reportsEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.github.babkiniaa.scas.entity.OWASPVulnerabilities;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "dependency")
@Table
public class DependencyCustom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    @OneToMany(cascade = CascadeType.ALL)
    private List<OWASPVulnerabilities> owaspVulnerabilities;

}
