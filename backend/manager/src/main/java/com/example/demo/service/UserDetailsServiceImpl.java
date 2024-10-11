package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Реализация сервиса для загрузки деталей пользователя по имени пользователя.
 *
 * <p>Этот класс отвечает за загрузку информации о пользователе из базы данных
 * с использованием {@link UserRepository} и предоставляет детали пользователя
 * для аутентификации.</p>
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    System.out.println("sad" + username);
    User user = userRepository.findByEmailOrLogin(username, username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    if (!user.isEnable()) {
      throw new UsernameNotFoundException("Email not verified");
    }

    Set<GrantedAuthority> authorities = user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toSet());

    return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            authorities);

  }
}
