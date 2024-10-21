package org.github.babkiniaa.scas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.Date;

@Setter
@Getter
@Table
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nameProject;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String url;

    private boolean visibility;


    private Date date;

    @OneToOne
    @JoinColumn(name = "report_id")
    private Report report;
//    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinColumn(name = "user_id")
//    private User user;

}
