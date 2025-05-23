package org.github.babkiniaa.scas.entity.reportsEntity.DeadCodeEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "die_dead")
public class DieDead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String className;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SummaryCustom> summaries;


}