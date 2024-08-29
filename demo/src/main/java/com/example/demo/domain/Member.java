package com.example.demo.domain;

import com.example.demo.oauth2.OAuth2Provider;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member extends DateBaseEntity{
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private LocalDateTime lastLoginAt;

    private String imageUrl;

    private String oAuth2Provider;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Authority> authorities = new ArrayList<>();

    public Member(String name, String email, String imageUrl, OAuth2Provider oAuth2Provider, List<EAuthority> authorities) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.oAuth2Provider = oAuth2Provider.name();
        this.lastLoginAt = LocalDateTime.now();
        authorities.forEach(this::addAuthority);
    }

    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }

    private void addAuthority(EAuthority eAuthority) {
        this.authorities.add(new Authority(eAuthority, this));
    }
}
