package org.github.babkiniaa.scas.repository;

import org.github.babkiniaa.scas.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {


    Page<Project> findByUserId(long userId, Pageable pageable);

    Page<Project> findByUserIdAndVisibility(Object unknownAttr1, boolean visibility, Pageable pageable);
}
