package org.github.babkiniaa.scas.dto.project;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO для получения ив всех  проектах пользователя
 */
@Getter
@Setter
public class GetProjectAllDto {

    private int count;

    private int page;

    private String sortingField = "date";

    private long userId;

    private boolean myProject;

    private String name;

    private String sortDirection;
}
