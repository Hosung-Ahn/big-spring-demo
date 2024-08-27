package com.example.demo.security.data;

import lombok.Data;

@Data
public class JwtAuthData {
    private Long memberId;
    private String accessToken;
    private String refreshToken;
    private Long refreshTokenExpirationFromNowInMS;

    public JwtAuthData(Long memberId, String accessToken, String refreshToken, Long refreshTokenExpirationFromNowInMS) {
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.refreshTokenExpirationFromNowInMS = refreshTokenExpirationFromNowInMS;
    }
}
