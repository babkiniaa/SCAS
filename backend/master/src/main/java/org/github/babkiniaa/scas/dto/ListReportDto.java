package org.github.babkiniaa.scas.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Dto для выдачи пользователю списка очётов, которые есть по данному проекту
 */
@Getter
@Setter
public class ListReportDto {

    private long id;
    private String branch;
    private LocalDateTime createdDate;
}
