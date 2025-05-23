package org.github.babkiniaa.scas.dto.reportsDto.DeadCodeDto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SummaryCustomDto {

    private String methodOrField;//Название метода

    private Double result; //Процент мёртвого кода

    private List<String> deadNodes; //Участки мёртвого кода

    private int before; //Цикломатическая сложность до упрощения

    private int after; //Цикломатическая сложность после упрощения

    private List<String> moduleNames; //Используемые модули

    private List<Double> moduleUsage; // Процент использования в коде
}
