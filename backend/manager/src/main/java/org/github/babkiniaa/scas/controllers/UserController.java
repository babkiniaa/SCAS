package org.github.babkiniaa.scas.controllers;

import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.ProfileDto;
import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.exception.NotFoundUser;
import org.github.babkiniaa.scas.mappers.UserMapper;
import org.github.babkiniaa.scas.security.AuthenticationFacade;
import org.github.babkiniaa.scas.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для обработки запросов, связанных с пользователями.
 * Обеспечивает маршрутизацию на домашнюю страницу.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:9000")
public class UserController {

    private final UserService userService;
    private  final UserMapper userMapper;
    private final AuthenticationFacade authenticationFacade;

    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> getProfile() throws NotFoundUser {
        String username = authenticationFacade.getCurrentUserName();
        System.out.println(username);
        User user = userService.findByUsername(username).orElseThrow(() -> new NotFoundUser("Пользователь не найден"));
        return ResponseEntity.ok(userMapper.toProfile(user));
    }
}
