package org.github.babkiniaa.scas.repository;

import org.github.babkiniaa.scas.entity.ReportCheckStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportCheckStyleRepository extends JpaRepository<ReportCheckStyle, Integer> {
}
