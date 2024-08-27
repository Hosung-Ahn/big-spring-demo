package com.example.demo.jwt;

import com.example.demo.security.exception.InvalidJwtException;
import com.example.demo.security.repository.AccessTokenRepository;
import com.example.demo.security.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class JwtValidator {
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtClaimReader jwtClaimReader;

    private void validateToken(String jwtToken) {
        try {
            jwtClaimReader.getClaims(jwtToken);
        } catch (SignatureException e) {
            throw new InvalidJwtException("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            throw new InvalidJwtException("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            throw new InvalidJwtException("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            throw new InvalidJwtException("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            throw new InvalidJwtException("JWT claims string is empty.");
        }
    }

    public void validateRefreshToken(String refreshToken) {
        try {
            validateToken(refreshToken);
        } catch (InvalidJwtException e) {
            throw new InvalidJwtException("refresh token is invalid.");
        }
        if (!refreshTokenRepository.isExist(refreshToken)) {
            throw new InvalidJwtException("refresh token expired or deleted.");
        }
    }

    public void validateAccessToken(String accessToken) {
        validateToken(accessToken);
        if (!accessTokenRepository.isExist(accessToken)) {
            throw new InvalidJwtException("access token expired or deleted.");
        }
    }
}
