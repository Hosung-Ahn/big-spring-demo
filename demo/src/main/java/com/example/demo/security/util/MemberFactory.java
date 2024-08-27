package com.example.demo.security.util;

import com.example.demo.domain.EAuthority;
import com.example.demo.domain.Member;
import com.example.demo.oauth2.OAuth2Provider;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberFactory {
    private final MemberRepository memberRepository;

    public Member createUser(String name, String email, String imageUrl, OAuth2Provider oAuth2Provider) {
        Member member = new Member(name, email, imageUrl, oAuth2Provider, List.of(EAuthority.ROLE_USER));
        return memberRepository.save(member);
    }
}
