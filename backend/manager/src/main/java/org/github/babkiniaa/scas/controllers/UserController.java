package org.github.babkiniaa.scas.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.dto.forUserDto.ProfileDto;
import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.exception.NotFoundUserException;
import org.github.babkiniaa.scas.mappers.UserMapper;
import org.github.babkiniaa.scas.security.AuthenticationFacade;
import org.github.babkiniaa.scas.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Контроллер для обработки запросов, связанных с пользователями.
 * Обеспечивает работу с профилем пользователя и аватаром.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:9000")
@Tag(name = "User Controller", description = "Методы для обычных пользователей: профиль, обновление и аватар")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationFacade authenticationFacade;

    @Operation(summary = "Получить профиль пользователя по ID", description = "Возвращает профиль пользователя по его идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль успешно получен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}/profile")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable("id") long id) throws NotFoundUserException {
        User user = userService.findById(id)
                .orElseThrow(() -> new NotFoundUserException("Пользователь не найден"));
        return ResponseEntity.ok(userMapper.toProfile(user));
    }

    @Operation(summary = "Редактировать профиль текущего пользователя", description = "Обновляет профиль авторизованного пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    @PutMapping("/profile")
    public ResponseEntity<?> editProfile(@RequestBody @Valid ProfileDto profileDto) throws NotFoundUserException {
        String username = authenticationFacade.getCurrentUserName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundUserException("Пользователь не найден"));
        userService.update(userMapper.updateUserFromDto(profileDto, user));
        return ResponseEntity.ok("ok");
    }

    @Operation(summary = "Загрузить аватар пользователя", description = "Позволяет загрузить файл аватара для текущего пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Аватар успешно загружен"),
            @ApiResponse(responseCode = "400", description = "Ошибка загрузки файла"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file) throws Exception {
        String username = authenticationFacade.getCurrentUserName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundUserException("Пользователь не найден"));
        userService.installAvatar(user, file);
        return ResponseEntity.ok("Avatar uploaded successfully");
    }

    @Operation(summary = "Удалить аватар пользователя", description = "Удаляет текущий аватар авторизованного пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Аватар успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("/avatar")
    public ResponseEntity<String> deleteAvatar() throws Exception {
        String username = authenticationFacade.getCurrentUserName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundUserException("Пользователь не найден"));
        userService.deleteAvatar(user);
        return ResponseEntity.ok("Avatar deleted successfully");
    }

    @Operation(summary = "Получить аватар пользователя по ID", description = "Возвращает аватар пользователя по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Аватар успешно получен"),
            @ApiResponse(responseCode = "404", description = "Пользователь или аватар не найден")
    })
    @GetMapping("/{id}/avatar")
    public ResponseEntity<?> getAvatar(@PathVariable("id") long id) {
        return ResponseEntity.ok(userService.getAvatar(id));
    }

    @Operation(summary = "Получить ID текущего пользователя", description = "Возвращает идентификатор авторизованного пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ID пользователя успешно получен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    @GetMapping("/get-id")
    public long getId() throws NotFoundUserException {
        return userService.findByUsername(authenticationFacade.getCurrentUserName())
                .orElseThrow(() -> new NotFoundUserException("User not found"))
                .getId();
    }
}
