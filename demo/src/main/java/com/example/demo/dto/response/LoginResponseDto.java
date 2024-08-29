package com.example.demo.dto.response;

import com.example.demo.domain.Member;
import com.example.demo.security.data.JwtAuthData;
import lombok.Data;

@Data
public class LoginResponseDto {
    private Long memberId;
    private boolean firstLogin;
    private String accessToken;
    private Long refreshTokenExpireTime;

    public LoginResponseDto(Member member, JwtAuthData jwtAuthData, boolean firstLogin) {
        this.memberId = member.getId();
        this.firstLogin = firstLogin;
        this.accessToken = jwtAuthData.getAccessToken();
        this.refreshTokenExpireTime = jwtAuthData.getRefreshTokenExpirationFromNowInMS();
    }
}
