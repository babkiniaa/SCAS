package org.github.babkiniaa.scas.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.entity.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;

@Data
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String KEY_FOR_USER_ID = "id";

    private static final String KEY_FOR_USER_EMAIL = "email";

    private static final String KEY_FOR_USER_ROLE = "role";

    private static final String START_FOR_USER_ROLE = "ROLE_";

    @Value("${security.jwt.secret}")
    private String key;

    @Value("${security.jwt.access}")
    private long access;

    @Value("${security.jwt.refresh}")
    private long refresh;

    /**
     * Получает секретный ключ для подписи JWT токенов.
     * Ключ декодируется из строки в формате BASE64.
     *
     * @return секретный ключ для HMAC-SHA подписи
     */
    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(getKey());

        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Создаёт access токен на основе данных пользователя.
     *
     * @param userId идентификатор пользователя
     * @param email  email пользователя, который будет храниться в subject токена
     * @param role   роль пользователя
     * @return сгенерированный access токен
     */
    public String createAccessToken(long userId, String email, Role role) {
        Claims claims = Jwts
                .claims()
                .subject(email)
                .add(KEY_FOR_USER_ID, userId)
                .add(KEY_FOR_USER_ROLE, START_FOR_USER_ROLE + role.name())
                .build();
        Date now = new Date();
        Date validity = new Date(now.getTime() + getAccess());

        return Jwts
                .builder()
                .claims(claims)
                .expiration(Date.from(validity.toInstant()))
                .signWith(getSigningKey()).compact();
    }

    /**
     * Создаёт refresh токен на основе данных пользователя.
     *
     * @param userId идентификатор пользователя
     * @param email  email пользователя, который будет храниться в subject токена
     * @return сгенерированный refresh токен
     */
    public String createRefreshToken(long userId, String email) {
        Claims claims = Jwts.claims().subject(email).add(KEY_FOR_USER_ID, userId).build();
        Date now = new Date();
        Date validity = new Date(now.getTime() + getAccess());

        return Jwts
                .builder()
                .claims(claims)
                .expiration(Date.from(validity.toInstant()))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Проверяет валидность JWT токена.
     * Извлекает данные из токена и проверяет, не истёк ли срок его действия.
     *
     * @param token JWT токен
     * @return true, если токен действителен, иначе false
     */
    public boolean isValid(String token) {
        Jws<Claims> claims = Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);

        return claims.getPayload().getExpiration().after(new Date());
    }
}