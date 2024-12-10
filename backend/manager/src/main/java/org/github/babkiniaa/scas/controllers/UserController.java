package org.github.babkiniaa.scas.controllers;

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
 * Обеспечивает маршрутизацию на домашнюю страницу.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:9000")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationFacade authenticationFacade;

    /**
     * Возвращает профиль пользователя по его идентификатору.
     * Если пользователь не найден, выбрасывается исключение.
     *
     * @param id идентификатор пользователя, чей профиль требуется получить
     * @return ResponseEntity, содержащий данные профиля пользователя (ProfileDto)
     * @throws NotFoundUserException если пользователь с указанным идентификатором не найден
     */
    @GetMapping("/profile/{id}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable("id") long id) throws NotFoundUserException {
        User user = userService.findById(id)
                .orElseThrow(() -> new NotFoundUserException("Пользователь не найден"));
        return ResponseEntity.ok(userMapper.toProfile(user));
    }

    /**
     * Обновляет профиль текущего аутентифицированного пользователя.
     * Данные пользователя передаются в теле запроса.
     * Если пользователь не найден по текущему имени пользователя, выбрасывается исключение.
     *
     * @param profileDto данные для обновления профиля пользователя
     * @return ResponseEntity с результатом операции (строка "ok")
     * @throws NotFoundUserException если текущий пользователь не найден
     */
    @PutMapping("/profile")
    public ResponseEntity<?> editProfile(@RequestBody @Valid ProfileDto profileDto) throws NotFoundUserException {
        String username = authenticationFacade.getCurrentUserName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundUserException("Пользователь не найден"));
        userService.update(userMapper.updateUserFromDto(profileDto, user));

        return ResponseEntity.ok("ok");
    }

    /**
     * Загружает аватарку для текущего аутентифицированного пользователя.
     * Аватарка передается в виде файла в теле запроса.
     * Если пользователь не найден по текущему имени пользователя, выбрасывается исключение.
     *
     * @param file файл аватарки, загружаемой пользователем
     * @return ResponseEntity с результатом операции (строка "Avatar uploaded successfully")
     * @throws NotFoundUserException если текущий пользователь не найден
     * @throws Exception если произошла ошибка при загрузке файла
     */
    @PostMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file) throws Exception {
        String username = authenticationFacade.getCurrentUserName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundUserException("Пользователь не найден"));

        userService.installAvatar(user, file);

        return ResponseEntity.ok("Avatar uploaded successfully");
    }

    /**
     * Удаляет аватарку текущего аутентифицированного пользователя.
     * Если пользователь не найден по текущему имени пользователя, выбрасывается исключение.
     *
     * @return ResponseEntity с результатом операции (строка "Avatar deleted successfully")
     * @throws NotFoundUserException если текущий пользователь не найден
     * @throws Exception если произошла ошибка при удалении аватарки
     */
    @DeleteMapping("/avatar")
    public ResponseEntity<String> deleteAvatar() throws Exception{
        String username = authenticationFacade.getCurrentUserName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new NotFoundUserException("Пользователь не найден"));

        userService.deleteAvatar(user);

        return ResponseEntity.ok("Avatar deleted successfully");
    }

    /**
     * Получает аватарку пользователя по идентификатору.
     * Если пользователь с данным идентификатором не найден, возвращается ошибка.
     *
     * @param id идентификатор пользователя, чью аватарку необходимо получить
     * @return ResponseEntity с аватаркой пользователя
     */
    @GetMapping("/avatar/{id}")
    public ResponseEntity<?> getAvatar(@PathVariable("id") long id){

        return ResponseEntity.ok(userService.getAvatar(id));
    }

    @GetMapping("get-id")
    public long getId() throws NotFoundUserException {

        return userService.findByUsername(authenticationFacade.getCurrentUserName())
                .orElseThrow(() -> new NotFoundUserException("User not found")).getId();
    }

}
