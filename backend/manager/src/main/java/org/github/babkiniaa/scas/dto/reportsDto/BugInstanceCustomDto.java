package org.github.babkiniaa.scas.dto.reportsDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BugInstanceCustomDto {

    private String type;

    private int priority;

    private int cachedHashCode;

    private String category;

    private String message;

    private String source;

    private String instanceHash;
}
