package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public String registerUser(User user) {
        user.setVerificationCode(generateVerificationCode());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.getRoles().add(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
        return user.getVerificationCode();
    }

    private String generateVerificationCode()   {
        return StringUtils.randomAlphanumeric(64);
    }

    @Transactional
    public boolean verify(String verificationCode) {
        User user = getByVerifyCode(verificationCode);
        if (user == null || user.isEnable()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnable(true);
            userRepository.save(user);
            return true;
        }
    }

    @Transactional
    public boolean verifyForChangePassword(String verificationCode) {
        User user = getByVerifyCode(verificationCode);
        if (user == null) {
            return false;
        } else {
            user.setVerificationCode(null);
            userRepository.save(user);
            return true;
        }
    }

    @Transactional
    public String changePassword(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setVerificationCode(generateVerificationCode());
        userRepository.save(user);
        return user.getVerificationCode();
    }

    public User getByVerifyCode(String code) {
        return userRepository.findByVerificationCode(code);
    }

    public void changePasswordUser(User user, String password) {
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setVerificationCode(null);
        userRepository.save(user);
    }
}
