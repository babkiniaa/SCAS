package org.github.babkiniaa.scas.dto.forUserDto;

import lombok.Data;
import org.github.babkiniaa.scas.entity.Role;

/**
 * DTO для ответа на вход в систему
 */
@Data
public class JwtResponse {

    private long currentId;

    private String accessToken;

    private String refreshToken;

    private String role;
}