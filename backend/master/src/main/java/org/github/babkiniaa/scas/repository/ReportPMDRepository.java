package org.github.babkiniaa.scas.repository;

import org.github.babkiniaa.scas.entity.ReportPMD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportPMDRepository extends JpaRepository<ReportPMD, Integer> {
}
