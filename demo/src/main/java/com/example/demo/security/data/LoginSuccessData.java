package com.example.demo.security.data;

import com.example.demo.domain.Member;
import lombok.Data;

@Data
public class LoginSuccessData {
    Member member;
    boolean firstLogin;
    JwtAuthData jwtAuthData;

    public LoginSuccessData(Member member, boolean firstLogin, JwtAuthData jwtAuthData) {
        this.member = member;
        this.firstLogin = firstLogin;
        this.jwtAuthData = jwtAuthData;
    }
}
