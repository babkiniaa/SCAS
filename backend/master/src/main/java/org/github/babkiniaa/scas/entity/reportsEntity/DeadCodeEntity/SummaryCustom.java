package org.github.babkiniaa.scas.entity.reportsEntity.DeadCodeEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "summary_custom")
public class SummaryCustom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String methodOrField;

    private Double result;

    private List<String> deadNodes;

    private Integer before;

    private Integer after;

    private List<String> moduleNames;

    private List<Double> moduleUsage;
}