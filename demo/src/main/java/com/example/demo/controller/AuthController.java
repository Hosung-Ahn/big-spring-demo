package com.example.demo.controller;

import com.example.demo.dto.request.LoginRequestDto;
import com.example.demo.dto.response.LoginResponseDto;
import com.example.demo.oauth2.OAuth2Provider;
import com.example.demo.security.data.LoginSuccessData;
import com.example.demo.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        String code = loginRequestDto.getCode();
        OAuth2Provider provider = OAuth2Provider.valueOf(loginRequestDto.getProvider());

        LoginSuccessData login = authService.login(code, provider);
        String refreshToken = login.getJwtAuthData().getRefreshToken();
        long refreshTokenExpireTime = login.getJwtAuthData().getRefreshTokenExpirationFromNowInMS();
        HttpCookie refreshTokenCookie = createRefreshTokenCookie(refreshToken, refreshTokenExpireTime);

        return ResponseEntity
                .ok()
                .header("Set-Cookie", refreshTokenCookie.toString())
                .body(new LoginResponseDto(login));
    }

    private HttpCookie createRefreshTokenCookie(String refreshToken, long expireTime) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(expireTime)
                .build();
    }
}
