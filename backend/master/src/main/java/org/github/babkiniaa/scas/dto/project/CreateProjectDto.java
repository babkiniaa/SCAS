package org.github.babkiniaa.scas.dto.project;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CreateProjectDto {

    private int userId;
    private String name;
    private String description;
    private String url;
    private boolean visibility = true;
    private LocalDateTime createdDate;
}
