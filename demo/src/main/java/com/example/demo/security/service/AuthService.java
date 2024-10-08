package com.example.demo.security.service;

import com.example.demo.domain.Member;
import com.example.demo.jwt.JwtClaimReader;
import com.example.demo.jwt.JwtCreator;
import com.example.demo.jwt.JwtValidator;
import com.example.demo.oauth2.OAuth2Provider;
import com.example.demo.oauth2.service.OAuth2Service;
import com.example.demo.oauth2.util.userprofile.data.UserProfileData;
import com.example.demo.repository.MemberRepository;
import com.example.demo.security.data.JwtAuthData;
import com.example.demo.security.data.LoginSuccessData;
import com.example.demo.security.exception.OAuth2ProviderDuplicationException;
import com.example.demo.security.repository.AccessTokenRepository;
import com.example.demo.security.repository.RefreshTokenRepository;
import com.example.demo.security.util.AtRtCreator;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final MemberService memberService;
    private final OAuth2Service oAuth2Service;
    private final MemberRepository memberRepository;
    private final AtRtCreator atRtCreator;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtValidator jwtValidator;
    @Transactional
    public LoginSuccessData login(String code, OAuth2Provider provider) {
        UserProfileData userProfile = oAuth2Service.getUserProfile(code, provider);

        // 이미 가입된 회원이라면 회원 정보를 바로 반환하고
        // 가입되지 않은 회원이라면 가입 절차를 진행하고 회원정보를 반환합니다.
        AtomicBoolean firstLogin = new AtomicBoolean(true);

        Member member = memberRepository.findByEmail(userProfile.getEmail()).orElseGet(() -> {
            firstLogin.set(false);
            return memberService.registerUser(userProfile.getName(), userProfile.getEmail(), userProfile.getImageUrl(), provider);
        });

        member.updateLastLoginAt();

        // 가입된 유저의 소셜 사이트와 로그인한 소셜 사이트가 다르다면 예외를 발생시킵니다.
        if (!member.getOAuth2Provider().equals(provider.name())) {
            throw new OAuth2ProviderDuplicationException(member.getOAuth2Provider());
        }

        JwtAuthData jwtAuthData = atRtCreator.create(member);
        return new LoginSuccessData(member, firstLogin.get(), jwtAuthData);
    }

    public void logout(String accessToken, String refreshToken) {
        if (accessToken != null && refreshToken != null) {
            accessTokenRepository.delete(accessToken);
            refreshTokenRepository.delete(refreshToken);
        } else if (accessToken != null) {
            accessTokenRepository.getRt(accessToken).ifPresent(refreshTokenRepository::delete);
            accessTokenRepository.delete(accessToken);
        } else if (refreshToken != null) {
            refreshTokenRepository.getAt(refreshToken).ifPresent(accessTokenRepository::delete);
            refreshTokenRepository.delete(refreshToken);
        }
    }

    public JwtAuthData refresh(String refreshToken) {
        jwtValidator.validateRefreshToken(refreshToken);

        return atRtCreator.refresh(refreshToken);
    }
}
