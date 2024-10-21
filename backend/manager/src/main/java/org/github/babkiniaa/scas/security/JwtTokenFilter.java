package org.github.babkiniaa.scas.security;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtUserDetailService userDetailService;

    private static final String AUTHORIZATION = "Authorization";

    private static final String START_TOKEN = "Bearer ";

    /**
     * Фильтр для обработки JWT токенов.
     * Извлекает токен из заголовка Authorization и проверяет его валидность.
     * Если токен действителен, устанавливает аутентификацию в SecurityContext.
     */
    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) {
        String bearerToken = ((HttpServletRequest) servletRequest).getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(START_TOKEN)) {
            bearerToken = bearerToken.substring(7);
        }
        try {
            if (bearerToken != null && jwtTokenProvider.isValid(bearerToken)) {
                Authentication authentication = getAuthentication(bearerToken);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {

        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Извлекает email из токена JWT.
     *
     * @param token JWT токен
     * @return email пользователя, извлечённый из токена
     */
    private String getEmail(String token) {

        return Jwts.parser().verifyWith(jwtTokenProvider.getSigningKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    /**
     * Получает объект Authentication на основе токена.
     * Загружает данные пользователя и создаёт UsernamePasswordAuthenticationToken.
     *
     * @param token JWT токен
     * @return объект Authentication для пользователя
     */
    public Authentication getAuthentication(String token) {
        String email = getEmail(token);
        UserDetails userDetails = userDetailService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}