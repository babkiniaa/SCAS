package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;

    public String registerUser(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.getRoles().add(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
        return tokenService.createToken(user);
    }

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

    @Transactional
    public String changePassword(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return tokenService.createToken(user);
    }

    @Transactional
    public void changePasswordUser(User user, String password) {
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        tokenService.delete(tokenService.getByUser(user));
        userRepository.save(user);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    public void blockUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        user.setEnable(false);
        userRepository.save(user);
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void deleteExpiredUsers() {
        if (!tokenService.getByBeforeExpiryDate().getUser().isEnable()){
            userRepository.delete(tokenService.getByBeforeExpiryDate().getUser());
            tokenService.delete(tokenService.getByBeforeExpiryDate());
        }
    }
}
