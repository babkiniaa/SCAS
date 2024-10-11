package com.example.demo.repository;

import com.example.demo.entity.Token;
import com.example.demo.entity.User;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью {@link Token}.
 * Предоставляет методы для выполнения операций с базой данных,
 * включая доступ к токенам верификации пользователей.
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
  Optional<Token> findByVerificationToken(String token);

  void deleteByVerificationToken(String verificationCode);

  Token findByUser(User user);

  Token findTokenByExpiryDateBefore(LocalDateTime now);
}
