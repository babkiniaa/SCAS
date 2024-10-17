package org.github.babkiniaa.scas.repository;

import org.github.babkiniaa.scas.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByUrl(String url);
}
