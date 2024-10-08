package com.example.demo.security.filter;

import com.example.demo.security.util.AuthenticationCreator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final AuthenticationCreator authenticationCreator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = resolveToken(request);

        try {
            if (accessToken != null) {
                // UsernamePasswordAuthenticationToken 으로 authentication 객체를 만들어서 SecurityContextHolder 에 저장합니다.
                // (at 로 로그인 한 경우 앱 어디서든 context 에서 authentication 을 얻어올 수 있습니다.)
                // (authenticaton 의 principal 에는 memberId 가 들어있습니다.)
                // 그리고 다음 filter 에서 authentication 의 authorities 를 이용해 권한을 체크합니다.
                Authentication authentication = authenticationCreator.createByAccessToken(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
