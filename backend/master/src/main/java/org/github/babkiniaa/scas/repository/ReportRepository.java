package org.github.babkiniaa.scas.repository;

import org.github.babkiniaa.scas.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAllByProjectId(long ProjectId);
}
