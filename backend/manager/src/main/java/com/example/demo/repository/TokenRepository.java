package com.example.demo.repository;

import com.example.demo.entity.Token;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByVerificationToken(String token);

    void deleteByVerificationToken(String verificationCode);

    Token findByUser(User user);
    Token findTokenByExpiryDateBefore(LocalDateTime now);
}
