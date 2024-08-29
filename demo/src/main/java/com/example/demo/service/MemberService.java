package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.oauth2.OAuth2Provider;
import com.example.demo.repository.MemberRepository;
import com.example.demo.security.util.MemberFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberFactory memberFactory;
    private final MemberRepository memberRepository;

    public Member registerUser(String name, String email, String imageUrl, OAuth2Provider provider) {
        Member user = memberFactory.createUser(name, email, imageUrl, provider);
        return memberRepository.save(user);
    }


}
