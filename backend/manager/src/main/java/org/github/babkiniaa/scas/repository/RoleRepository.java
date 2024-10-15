package org.github.babkiniaa.scas.repository;

import org.github.babkiniaa.scas.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью {@link Role}.
 * Предоставляет методы для выполнения операций с базой данных,
 * включая доступ к ролям пользователей.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleName);
}
