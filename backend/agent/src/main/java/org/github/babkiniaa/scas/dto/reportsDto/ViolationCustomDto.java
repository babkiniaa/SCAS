package org.github.babkiniaa.scas.dto.reportsDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViolationCustomDto  implements Serializable {

    private int lineNo;

    private int columnNo;

    private int columnCharIndex;

    private int tokenType;

    private String moduleId;

    private String key;

    private String bundle;

    private String customMessage;

    private String source;


}
