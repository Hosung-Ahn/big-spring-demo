package com.example.demo.security.util;

import com.example.demo.domain.Member;
import com.example.demo.jwt.JwtClaimReader;
import com.example.demo.jwt.JwtCreator;
import com.example.demo.security.data.JwtAuthData;
import com.example.demo.security.repository.AccessTokenRepository;
import com.example.demo.security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AtRtCreator {
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtCreator jwtCreator;
    private final JwtClaimReader jwtClaimReader;

    public JwtAuthData create(Member member) {
        Long memberId = member.getId();
        Collection<GrantedAuthority> authorities = getAuthorities(member);

        return getAtRtDto(memberId, authorities);
    }

    public JwtAuthData refresh(String refreshToken) {
        Long memberId = jwtClaimReader.getMemberId(refreshToken);
        Collection<? extends GrantedAuthority> authorities = jwtClaimReader.getAuthorities(refreshToken);

        refreshTokenRepository.getAt(refreshToken).ifPresent(
                accessTokenRepository::delete
        );
        refreshTokenRepository.delete(refreshToken);

        return getAtRtDto(memberId, authorities);
    }

    private JwtAuthData getAtRtDto(Long memberId, Collection<? extends GrantedAuthority> authorities) {
        String newAccessToken = jwtCreator.createAccessToken(memberId, authorities);
        String newRefreshToken = jwtCreator.createRefreshToken(memberId, authorities);

        accessTokenRepository.mapAtToRt(newAccessToken, newRefreshToken);
        refreshTokenRepository.mapRtToAt(newRefreshToken, newAccessToken);

        Long expirationInMS = jwtClaimReader.getExpirationInMilliseconds(newRefreshToken);

        return new JwtAuthData(memberId, newAccessToken, newRefreshToken, expirationInMS);
    }

    private Collection<GrantedAuthority> getAuthorities(Member member) {
        return member.getAuthorities().stream().map(
                authority -> (GrantedAuthority) authority::getName
        ).toList();
    }
}
