package org.github.babkiniaa.scas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProjectDto {
    private int count;
    private int page;
    private String sortingField = "date";
    private long userId;
    private boolean myProject;
    private String name;
}
