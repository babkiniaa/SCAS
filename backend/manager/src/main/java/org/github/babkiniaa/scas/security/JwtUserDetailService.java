package org.github.babkiniaa.scas.security;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.mappers.JwtMapper;
import org.github.babkiniaa.scas.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

    private final UserService userService;

    private final JwtMapper jwtMapper;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByEmailOrUsername(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!user.isEnable()) {
            throw new UsernameNotFoundException("Email not verified");
        }
        return jwtMapper.toJwt(user);
    }

}
