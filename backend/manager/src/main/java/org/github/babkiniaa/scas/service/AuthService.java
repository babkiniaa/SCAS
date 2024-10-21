package org.github.babkiniaa.scas.service;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.JwtResponse;
import org.github.babkiniaa.scas.dto.LoginDto;
import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.exception.NotFoundUser;
import org.github.babkiniaa.scas.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Выполняет аутентификацию пользователя и генерирует JWT токены (access и refresh).
     * Использует данные, переданные в запросе, для аутентификации и возвращает токены.
     *
     * @param loginRequest объект с данными для входа (username и пароль)
     * @return объект JwtResponse, содержащий идентификатор пользователя, access и refresh токены
     * @throws NotFoundUser если пользователь не найден по email или имени пользователя
     */
    public JwtResponse login(LoginDto loginRequest) throws NotFoundUser {
        JwtResponse jwtResponse = new JwtResponse();

        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        User user = userService
                .findByEmailOrUsername(loginRequest.getUsername(), loginRequest.getUsername())
                .orElseThrow(() -> new NotFoundUser("Не найден пользователь"));
        jwtResponse.setCurrentId(user.getId());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(),  user.getEmail(), user.getRole()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail()));

        return jwtResponse;
    }

}
