package org.github.babkiniaa.scas.dto.ProjectAndId;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ProjectAndUserIdDto {

    private int userId;

    private String name;

    private String description;

    private String url;

    private boolean visibility = true;

    private LocalDateTime createdDate;
}
