package com.example.demo.controller.dto.response;

import com.example.demo.domain.Member;
import com.example.demo.security.data.JwtAuthData;
import com.example.demo.security.data.LoginSuccessData;
import lombok.Data;

@Data
public class LoginResponseDto {
    private Long memberId;
    private boolean firstLogin;
    private String accessToken;
    private Long refreshTokenExpireTime;

    public LoginResponseDto(LoginSuccessData loginSuccessData) {
        this.memberId = loginSuccessData.getMember().getId();
        this.firstLogin = loginSuccessData.isFirstLogin();
        this.accessToken = loginSuccessData.getJwtAuthData().getAccessToken();
        this.refreshTokenExpireTime = loginSuccessData.getJwtAuthData().getRefreshTokenExpirationFromNowInMS();
    }
}
