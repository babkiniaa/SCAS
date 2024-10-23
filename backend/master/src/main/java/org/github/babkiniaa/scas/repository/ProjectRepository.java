package org.github.babkiniaa.scas.repository;

import org.github.babkiniaa.scas.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью {@link Project}.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    /**
     * Находит проекты, содержащие указанное имя и принадлежащие определенному пользователю.
     *
     * @param name название проекта, по которому производится поиск.
     * @param userId идентификатор пользователя, которому принадлежат проекты.
     * @param pageable объект {@link Pageable} для задания параметров пагинации.
     * @return страница проектов, соответствующих критериям поиска.
     */
    Page<Project> findAllByNameContainingAndUserId(String name, long userId, Pageable pageable);

    /**
     * Находит публичные проекты, содержащие указанное имя, принадлежащие определенному пользователю.
     *
     * @param name название проекта, по которому производится поиск.
     * @param userId идентификатор пользователя, которому принадлежат проекты.
     * @param visibility видимость проекта (true - публичный, false - приватный).
     * @param pageable объект {@link Pageable} для задания параметров пагинации.
     * @return страница публичных проектов, соответствующих критериям поиска.
     */
    Page<Project> findByNameContainingAndUserIdAndVisibility(String name, long userId, boolean visibility, Pageable pageable);
}
