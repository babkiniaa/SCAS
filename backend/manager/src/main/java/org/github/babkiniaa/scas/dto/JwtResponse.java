package org.github.babkiniaa.scas.dto;

import lombok.Data;

@Data
public class JwtResponse {

    private long currentId;

    private String accessToken;

    private String refreshToken;
}