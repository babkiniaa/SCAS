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

    public JwtResponse login(LoginDto loginRequest) throws NotFoundUser {
        JwtResponse jwtResponse = new JwtResponse();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        User user = userService.findByEmailOrLogin(loginRequest.getUsername(), loginRequest.getUsername()).orElseThrow(() -> new NotFoundUser("Не найден пользователь"));
        jwtResponse.setUsername(user.getLogin());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getLogin(), user.getEmail(), user.getRole()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getLogin()));
        return jwtResponse;
    }
}
