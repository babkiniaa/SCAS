package org.github.babkiniaa.scas.entity.reportsEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "attentions")
@Table
public class AttentionsCustom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nameFile;

    private int line;
    @Lob
    private String code;

    private String description;

}
