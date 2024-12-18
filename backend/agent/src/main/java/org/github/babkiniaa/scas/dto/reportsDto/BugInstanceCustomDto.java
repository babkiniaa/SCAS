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
public class BugInstanceCustomDto implements Serializable{

    private String type;

    private int priority;

    private String category;

    private String message;

    private String source;

    private String instanceHash;

}
