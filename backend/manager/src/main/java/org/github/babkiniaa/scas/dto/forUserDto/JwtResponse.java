package org.github.babkiniaa.scas.dto.forUserDto;

import lombok.Data;

/**
 * DTO для ответа на вход в систему
 */
@Data
public class JwtResponse {

    private long currentId;

    private String accessToken;

    private String refreshToken;
}