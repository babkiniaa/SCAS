package org.github.babkiniaa.scas.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Getter
@Setter
public class GetProjectDto {
    private int count;
    private int page;
    private String sortingField = "date";
    private long userId;
    private boolean myProject;
}
