package com.example.demo.controller;

import com.example.demo.controller.dto.request.LoginRequestDto;
import com.example.demo.controller.dto.response.LoginResponseDto;
import com.example.demo.controller.dto.response.RefreshResponseDto;
import com.example.demo.oauth2.OAuth2Provider;
import com.example.demo.security.data.JwtAuthData;
import com.example.demo.security.data.LoginSuccessData;
import com.example.demo.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final String BEARER_PREFIX = "Bearer ";
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

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization", required = false) String accessTokenInHeader,
                                         @CookieValue(required = false) String refreshToken ) {
        String accessToken = getAccessToken(accessTokenInHeader);
        authService.logout(accessToken, refreshToken);
        return ResponseEntity.ok("logout success");
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDto> refresh(@CookieValue String refreshToken) {
        JwtAuthData refresh = authService.refresh(refreshToken);

        String newRefreshToken = refresh.getRefreshToken();
        long newRefreshTokenExpireTime = refresh.getRefreshTokenExpirationFromNowInMS();
        HttpCookie newRefreshTokenCookie = createRefreshTokenCookie(newRefreshToken, newRefreshTokenExpireTime);

        return ResponseEntity
                .ok()
                .header("Set-Cookie", newRefreshTokenCookie.toString())
                .body(new RefreshResponseDto(refresh));
    }

    private HttpCookie createRefreshTokenCookie(String refreshToken, long expireTime) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(expireTime)
                .build();
    }

    private String getAccessToken(String accessTokenInHeader) {
        if (accessTokenInHeader.length() < BEARER_PREFIX.length()) {
            return "";
        }
        return accessTokenInHeader.substring(BEARER_PREFIX.length());
    }
}
