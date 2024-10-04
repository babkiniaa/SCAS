package com.example.demo.service;

import com.example.demo.entity.Token;
import com.example.demo.entity.User;
import com.example.demo.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public Token getByBeforeExpiryDate(){
        return tokenRepository.findTokenByExpiryDateBefore(now());
    }


    private String generateVerificationCode()   {
        return StringUtils.randomAlphanumeric(64);
    }

    public String createToken(User user){
        Token token = new Token();
        token.setUser(user);
        token.setExpiryDate(now().plusHours(1));
        token.setVerificationToken(generateVerificationCode());
        tokenRepository.save(token);
        return token.getVerificationToken();
    }

    public Token getByVerifyCode(String verificationCode){
        return tokenRepository.findByVerificationToken(verificationCode).get();
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
