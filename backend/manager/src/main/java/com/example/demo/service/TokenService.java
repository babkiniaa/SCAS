package com.example.demo.service;

import com.example.demo.entity.Token;
import com.example.demo.entity.User;
import com.example.demo.repository.TokenRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;


/**
 * Сервис для управления токенами верификации пользователей.
 *
 * <p>Этот класс предоставляет методы для создания, получения и удаления токенов
 * верификации, связанных с пользователями. Токены используются для верификации
 * регистрации и сброса пароля.</p>
 */
@Service
@RequiredArgsConstructor
public class TokenService {

  private final TokenRepository tokenRepository;

  public Token getByBeforeExpiryDate() {
    return tokenRepository.findTokenByExpiryDateBefore(LocalDateTime.now());
  }


  private String generateVerificationCode() {
    return StringUtils.randomAlphanumeric(64);
  }

  /**
   * Создает новый токен для указанного пользователя.
   *
   * <p>Токен будет иметь срок действия 1 час и сгенерированный код верификации.</p>
   *
   * @param user пользователь, для которого создается токен
   * @return код верификации токена
   */
  public String createToken(User user) {
    Token token = new Token();
    token.setUser(user);
    token.setExpiryDate(LocalDateTime.now().plusHours(1));
    token.setVerificationToken(generateVerificationCode());
    tokenRepository.save(token);
    return token.getVerificationToken();
  }

  public Token getByVerifyCode(String verificationCode) {
    return tokenRepository.findByVerificationToken(verificationCode).orElseThrow();
  }

  public void deleteByToken(String verificationCode) {
    tokenRepository.deleteByVerificationToken(verificationCode);
  }

  public Token getByUser(User user) {
    return tokenRepository.findByUser(user);
  }

  public void delete(Token token) {
    tokenRepository.delete(token);
  }
}
