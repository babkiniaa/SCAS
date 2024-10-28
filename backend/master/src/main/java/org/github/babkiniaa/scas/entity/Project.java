package org.github.babkiniaa.scas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Table
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "project")
@EntityListeners(AuditingEntityListener.class)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String url;

    private boolean visibility = true;

    @Column(name = "created_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany
    @JoinColumn(name = "reportCheckStyle_id")
    private List<ReportCheckStyle> reportCheckStyles;

    @OneToMany
    @JoinColumn(name = "reportOWASP_id")
    private List<ReportOWASP> reportOWASPS;

    @OneToMany
    @JoinColumn(name = "reportPMD_id")
    private List<ReportPMD> reportPMDS;

    @OneToMany
    @JoinColumn(name = "reportSpotBugs_id")
    private List<ReportSpotBugs> reportSpotBugs;

    private long userId;
}
