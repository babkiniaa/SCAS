package org.github.babkiniaa.scas.repository;

import org.github.babkiniaa.scas.entity.ReportSpotBugs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportSpotBugsRepository extends JpaRepository<ReportSpotBugs, Integer> {
}
