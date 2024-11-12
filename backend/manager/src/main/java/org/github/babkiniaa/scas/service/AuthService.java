package org.github.babkiniaa.scas.service;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.forUserDto.JwtResponse;
import org.github.babkiniaa.scas.dto.forUserDto.LoginDto;
import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.exception.NotFoundUserException;
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
     * @throws NotFoundUserException если пользователь не найден по email или имени пользователя
     */
    public JwtResponse login(LoginDto loginRequest) throws NotFoundUserException {
        JwtResponse jwtResponse = new JwtResponse();

        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        User user = userService
                .findByEmailOrUsername(loginRequest.getUsername(), loginRequest.getUsername())
                .orElseThrow(() -> new NotFoundUserException("Не найден пользователь"));
        jwtResponse.setCurrentId(user.getId());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(),  user.getEmail(), user.getRole()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail()));

        return jwtResponse;
    }

}
