package com.example.demo.controller.dto.response;

import com.example.demo.security.data.JwtAuthData;
import lombok.Data;

@Data
public class RefreshResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long refreshTokenExpireTime;

    public RefreshResponseDto(JwtAuthData jwtAuthData) {
        this.accessToken = jwtAuthData.getAccessToken();
        this.refreshToken = jwtAuthData.getRefreshToken();
        this.refreshTokenExpireTime = jwtAuthData.getRefreshTokenExpirationFromNowInMS();
    }
}
