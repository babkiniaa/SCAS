package org.github.babkiniaa.scas.repository;

import org.github.babkiniaa.scas.entity.ReportPmd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportPmdRepository extends JpaRepository<ReportPmd, Integer> {
}