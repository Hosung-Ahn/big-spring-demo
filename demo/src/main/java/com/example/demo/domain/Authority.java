package com.example.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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


}
