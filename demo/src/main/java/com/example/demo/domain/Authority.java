package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Authority {
    @Id @GeneratedValue
    @Column(name = "authority_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    public Authority(EAuthority eAuthority, Member member) {
        this.name = eAuthority.name();
        this.member = member;
    }
}
