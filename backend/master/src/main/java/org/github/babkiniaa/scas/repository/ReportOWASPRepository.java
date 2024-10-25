package org.github.babkiniaa.scas.repository;

import org.github.babkiniaa.scas.entity.ReportOWASP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportOWASPRepository extends JpaRepository<ReportOWASP, Integer> {
}
