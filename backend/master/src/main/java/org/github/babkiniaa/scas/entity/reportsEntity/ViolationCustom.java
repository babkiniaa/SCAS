package org.github.babkiniaa.scas.entity.reportsEntity;

import com.puppycrawl.tools.checkstyle.api.SeverityLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "violation")
@Table
public class ViolationCustom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int lineNo;

    private int columnNo;

    private int columnCharIndex;

    private int tokenType;

    private String moduleId;

    @Column(name = "keyValue")
    private String key;

    private String bundle;

    private String customMessage;


}
