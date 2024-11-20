package org.github.babkiniaa.scas.service;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.List;
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

    private final MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.url}")
    private String url;


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
        if (tokenService.getByVerifyCode(verificationCode) == null){
            return false;
        }
        User user = tokenService.getByVerifyCode(verificationCode).getUser();
        user.setEnable(true);
        userRepository.save(user);
        tokenService.deleteByToken(verificationCode);

        return true;
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
        return tokenService.createTokenForPassword(user);
    }

    /**
     * Изменяет пароль пользователя.
     *
     * <p>Шифрует новый пароль и сохраняет его в базе данных,
     * а также удаляет старый токен.</p>
     *
     * @param user     объект пользователя, чей пароль нужно изменить
     * @param password новый пароль
     */
    @Transactional
    public void changePasswordUser(User user, String password) {
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        tokenService.delete(tokenService.getByUser(user));
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
      return userRepository.findAll();
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
        var expiredToken = tokenService.getByBeforeExpiryDate();

        if (expiredToken != null && expiredToken.getUser() != null && !expiredToken.getUser().isEnable()) {
          userRepository.delete(expiredToken.getUser());
          tokenService.delete(expiredToken);
        }
    }

    public Optional<User> findByEmailOrUsername(String email, String username) {
      return userRepository.findByEmailOrUsername(email, username);
    }

    /**
     * Находит пользователя по имени пользователя (username).
     *
     * @param username имя пользователя (username)
     * @return Optional с найденным пользователем или пустое значение, если пользователь не найден
     */
    public Optional<User> findByUsername(String username) {
      return userRepository.findByUsername(username);
    }

    /**
     * Обновляет данные пользователя в базе данных.
     * Метод оборачивается в транзакцию, чтобы изменения были атомарными.
     *
     * @param user пользователь, данные которого нужно обновить
     */
    @Transactional
    public void update(User user) {
      userRepository.save(user);
    }

    /**
     * Находит пользователя по email.
     *
     * @param email email пользователя
     * @return Optional с найденным пользователем или пустое значение, если пользователь не найден
     */
    public Optional<User> findByEmail(String email) {
      return userRepository.findByEmail(email);
    }

    /**
     * Находит пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return Optional с найденным пользователем или пустое значение, если пользователь не найден
     */
    public Optional<User> findById(long id) {
      return userRepository.findById(id);
    }

    /**
     * Обновляет URL аватара для указанного пользователя.
     * <p>
     * Устанавливает новый URL аватара и сохраняет обновленного пользователя в
     * репозитории.
     * </p>
     *
     * @param user      пользователь, для которого обновляется URL аватара
     * @param avatarUrl новый URL аватара
     */
    public void updateAvatarUrl(User user, String avatarUrl){
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
    }

    /**
     * Получает URL аватара пользователя по его идентификатору.
     * <p>
     * Если пользователь с указанным идентификатором не найден, выбрасывается
     * исключение {@link UsernameNotFoundException}.
     * </p>
     *
     * @param id идентификатор пользователя
     * @return URL аватара пользователя
     * @throws UsernameNotFoundException если пользователь с указанным идентификатором не найден
     */
    public String getAvatar(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Пользоватеель не найден"));
        return user.getAvatarUrl();
    }

  /**
   * Удаляет аватар пользователя.
   * <p>
   * Удаляет объект аватара из хранилища MinIO и обновляет URL аватара в
   * профиле пользователя, устанавливая его в {@code null}.
   * </p>
   *
   * @param user пользователь, для которого удаляется аватар
   * @throws Exception если возникла ошибка при удалении объекта из MinIO
   */
    public void deleteAvatar(User user) throws Exception {
        String avatarUrl = user.getAvatarUrl();
        String objectName = avatarUrl.substring(avatarUrl.lastIndexOf("/") + 1);
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        updateAvatarUrl(user, null);
    }

  /**
   * Устанавливает аватар для пользователя, загружая файл в MinIO.
   * <p>
   * Если корзина не существует, она создается. Загруженный файл
   * сохраняется под уникальным именем, основанным на оригинальном имени файла.
   * Обновляется URL аватара пользователя.
   * </p>
   *
   * @param user пользователь, которому устанавливается аватар
   * @param file файл аватара, загружаемый в MinIO
   * @throws Exception если возникла ошибка при загрузке файла в MinIO
   */
    public void installAvatar(User user, MultipartFile file) throws Exception{
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
          minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        String objectName = "avatar_" + file.getOriginalFilename();
        InputStream fileInputStream = file.getInputStream();
        minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                                fileInputStream, file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
        updateAvatarUrl(user, this.url + "/" + this.bucketName + "/" + objectName);
    }
}
