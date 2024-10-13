package org.github.babkiniaa.scas.repository;

import org.github.babkiniaa.scas.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью {@link User}.
 * Предоставляет методы для выполнения операций с базой данных,
 * включая доступ к пользователям и их учетным данным.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailOrLogin(String email, String login);

    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);

}
