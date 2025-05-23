package org.github.babkiniaa.scas.dto.reportsDto.DeadCodeDto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DieDeaDto {
    private String className;

    private List<SummaryDto> summaries;
}
