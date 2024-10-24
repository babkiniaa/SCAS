package org.github.babkiniaa.scas.controllers;

import io.minio.errors.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.ProfileDto;
import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.exception.NotFoundUser;
import org.github.babkiniaa.scas.mappers.UserMapper;
import org.github.babkiniaa.scas.security.AuthenticationFacade;
import org.github.babkiniaa.scas.service.AvatarService;
import org.github.babkiniaa.scas.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
    private final UserMapper userMapper;
    private final AvatarService avatarService;
    private final AuthenticationFacade authenticationFacade;

    /**
     * Возвращает профиль пользователя по его идентификатору.
     * Если пользователь не найден, выбрасывается исключение.
     *
     * @param id идентификатор пользователя, чей профиль требуется получить
     * @return ResponseEntity, содержащий данные профиля пользователя (ProfileDto)
     * @throws NotFoundUser если пользователь с указанным идентификатором не найден
     */
    @GetMapping("/profile/{id}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable("id") long id) throws NotFoundUser {
        User user = userService.findById(id)
                .orElseThrow(() -> new NotFoundUser("Пользователь не найден"));

        return ResponseEntity.ok(userMapper.toProfile(user));
    }

    /**
     * Обновляет профиль текущего аутентифицированного пользователя.
     * Данные пользователя передаются в теле запроса.
     * Если пользователь не найден по текущему имени пользователя, выбрасывается исключение.
     *
     * @param profileDto данные для обновления профиля пользователя
     * @return ResponseEntity с результатом операции (строка "ok")
     * @throws NotFoundUser если текущий пользователь не найден
     */
    @PutMapping("/profile")
    public ResponseEntity<?> editProfile(@RequestBody @Valid ProfileDto profileDto) throws NotFoundUser {
        String username = authenticationFacade.getCurrentUserName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundUser("Пользователь не найден"));
        userService.update(userMapper.updateUserFromDto(profileDto, user));

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/{userId}/avatar")
    public ResponseEntity<User> uploadUserAvatar(@PathVariable Long userId, @RequestParam("file") MultipartFile file)
            throws NotFoundUser, ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        String avatarUrl = avatarService.uploadAvatar(file);
        User updatedUser = userService.updateUserAvatar(userId, avatarUrl);
        return ResponseEntity.ok(updatedUser);
    }
}
