package org.github.babkiniaa.scas.service;

import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Сервис для управления пользователями.
 *
 * <p>Этот класс предоставляет методы для регистрации пользователей,
 * проверки их учетных записей, изменения паролей и управления
 * пользователями в базе данных.</p>
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    /**
     * Регистрирует нового пользователя.
     *
     * <p>Шифрует пароль пользователя, добавляет роль по умолчанию и
     * создает токен для верификации.</p>
     *
     * @param user объект пользователя для регистрации
     * @return строка, представляющая токен верификации
     */
    public String registerUser(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return tokenService.createToken(user);
    }

    /**
     * Проверяет учетную запись пользователя по верификационному коду.
     *
     * <p>Активирует учетную запись пользователя и удаляет токен верификации.</p>
     *
     * @param verificationCode код для верификации пользователя
     * @return {@code true}, если верификация прошла успешно
     */
    @Transactional
    public boolean verify(String verificationCode) {
        User user = tokenService.getByVerifyCode(verificationCode).getUser();
        user.setEnable(true);
        userRepository.save(user);
        tokenService.deleteByToken(verificationCode);
        return true;
    }

    @Transactional
    public boolean verifyForChangePassword(String verificationCode) {
        return tokenService.getByVerifyCode(verificationCode) != null;
    }

    /**
     * Генерирует новый токен для изменения пароля.
     *
     * <p>Ищет пользователя по электронной почте и создает токен.</p>
     *
     * @param email адрес электронной почты пользователя
     * @return строка, представляющая токен для изменения пароля
     */
    @Transactional
    public String changePassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return tokenService.createToken(user);
    }

    /**
     * Изменяет пароль пользователя.
     *
     * <p>Шифрует новый пароль и сохраняет его в базе данных,
     * а также удаляет старый токен.</p>
     *
     * @param user объект пользователя, чей пароль нужно изменить
     * @param password новый пароль
     */
    @Transactional
    public void changePasswordUser(User user, String password) {
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        tokenService.delete(tokenService.getByUser(user));
        userRepository.save(user);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Блокирует пользователя по его идентификатору.
     *
     * <p>Устанавливает флаг активности пользователя в {@code false}.</p>
     *
     * @param userId идентификатор пользователя, которого нужно заблокировать
     */
    @Transactional
    public void blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        user.setEnable(false);
        userRepository.save(user);
    }

    /**
     * Удаляет пользователей, у которых время на подтверждение почты истекло.
     * <p>Запланированное выполнение каждый час. Удаляет пользователей,
     * у которых истек токен верификации.</p>
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void deleteExpiredUsers() {
        if (!tokenService.getByBeforeExpiryDate().getUser().isEnable()) {
            userRepository.delete(tokenService.getByBeforeExpiryDate().getUser());
            tokenService.delete(tokenService.getByBeforeExpiryDate());
        }
    }


    public Optional<User> findByEmailOrUsername(String email, String username) {
        return userRepository.findByEmailOrUsername(email, username);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
