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


    public String registerUser(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);

        return tokenService.createToken(user);
    }


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


    @Transactional
    public String changePassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return tokenService.createTokenForPassword(user);
    }


    @Transactional
    public void changePasswordUser(User user, String password) {
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        tokenService.delete(tokenService.getByUser(user));
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
      return userRepository.findAll();
    }


    @Transactional
    public void blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        user.setEnable(false);
        userRepository.save(user);
    }

    @Transactional
    public void unBlockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        user.setEnable(true);
        userRepository.save(user);
    }


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


    public Optional<User> findByUsername(String username) {
      return userRepository.findByUsername(username);
    }


    @Transactional
    public void update(User user) {
      userRepository.save(user);
    }


    public Optional<User> findByEmail(String email) {
      return userRepository.findByEmail(email);
    }


    public Optional<User> findById(long id) {
      return userRepository.findById(id);
    }


    public void updateAvatarUrl(User user, String avatarUrl){
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
    }


    public String getAvatar(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Пользоватеель не найден"));
        return user.getAvatarUrl();
    }


    public void deleteAvatar(User user) throws Exception {
        String avatarUrl = user.getAvatarUrl();
        String objectName = avatarUrl.substring(avatarUrl.lastIndexOf("/") + 1);
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        updateAvatarUrl(user, null);
    }


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
