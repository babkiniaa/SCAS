package org.github.babkiniaa.scas.dto.reportsDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sourceforge.pmd.lang.document.FileId;

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

}
